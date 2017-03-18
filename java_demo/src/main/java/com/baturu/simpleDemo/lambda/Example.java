package com.baturu.simpleDemo.lambda;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by xuran on 2015/12/22.
 */
public class Example {

    @Test
    public void example1() {

        //Before Java 8:
        new Thread(new Runnable(){
            public void run() {
                System.out.println("Before Java8 ");
            }
        }).start();

        //Java 8 way:
        new Thread( () -> System.out.println("In Java8!")).start();
    }

    @Test
    public void example2() {
        //Prior Java 8 :
        List<String> features = Arrays.asList("Lambdas", "Default Method",
                "Stream API", "Date and Time API");
        for (String feature : features) {
            System.out.println(feature);
        }

        //In Java 8:
        features = Arrays.asList("Lambdas", "Default Method", "Stream API",
                "Date and Time API");
        features.forEach(str -> System.out.println(str));

        // Even better use Method reference feature of Java 8
        // method reference is denoted by :: (double colon) operator
        // looks similar to score resolution operator of C++
        features.forEach(System.out::println);
    }

    @Test
    public void example3() {
        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");

        System.out.println("Languages which starts with J :");
        filter(languages, (str)->((String)str).startsWith("J"));

        System.out.println("Languages which ends with a ");
        filter(languages, (str)->((String)str).endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str)->true);

        System.out.println("Print no language : ");
        filter(languages, (str)->false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str)->((String)str).length() > 4);
    }

    @Test
    public void example4() {
        // We can even combine Predicate using and(), or() And xor() logical functions
        // for example to find names, which starts with J and four letters long, you
        // can pass combination of two Predicate
        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;

        List<String> names = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        names.stream().filter(startsWithJ.and(fourLetterLong))
                .forEach((str) -> System.out.print("\nName, which starts with 'J' and four letter long is : " + str));
    }

    @Test
    public void example5() {
        // applying 12% VAT on each purchase
        // Without lambda expressions:
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12*cost;
            System.out.println(price);
        }

        // With Lambda expression:
        costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax.stream().map((cost) -> cost + .12*cost)
                .forEach(System.out::println);
    }

    @Test
    public void example6() {
        // Applying 12% VAT on each purchase
        // Old way:
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12*cost;
            total = total + price;

        }
        System.out.println("Total : " + total);

        // New way:
        costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double bill = costBeforeTax.stream().map((cost) -> cost + .12*cost)
                .reduce((sum, cost) -> sum + cost)
                .get();
        System.out.println("Total : " + bill);
    }

    @Test
    public void example7() {
        // Create a List with String more than 2 characters
        List<String> strList = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        List<String> filtered = strList.stream().filter(x -> x.length()> 4)
                .collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n",
                strList, filtered);
    }

    @Test
    public void example8() {
        // Convert String to Uppercase and join them using coma
        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany",
                "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase())
                .collect(Collectors.joining(", "));
        System.out.println(G7Countries);

    }

    @Test
    public void example9() {
        // Create List of square of all distinct numbers
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map( i -> i*i).distinct()
                .collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    @Test
    public void example10() {
        //Get count, min, max, sum, and average for numbers
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x)
                .summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    private static void filter(List<String> names, Predicate condition) {
        for(String name: names)  {
            if(condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    public void primaryOccurrence(String... numbers) {
        List<String> l = Arrays.asList(numbers);
        Map<Integer, Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> e > 5)
                .collect( Collectors.groupingBy(p->p, Collectors.summingInt(p->1)) );
        System.out.println("primaryOccurrence result is: " + r);
    }

    int tmp1 = 1; //包围类的成员变量
    static int tmp2 = 2; //包围类的静态成员变量
    public void testCapture() {
        int tmp3 = 3; //没有声明为final，但是effectively final的本地变量
        final int tmp4 = 4; //声明为final的本地变量
        int tmp5 = 5; //普通本地变量

        Function<Integer, Integer> f1 = i -> i + tmp1;
        Function<Integer, Integer> f2 = i -> i + tmp2;
        Function<Integer, Integer> f3 = i -> i + tmp3;
        Function<Integer, Integer> f4 = i -> i + tmp4;
        Function<Integer, Integer> f5 = i -> {
//            tmp5  += i; // 编译错！对tmp5赋值导致它不是effectively final的
            return tmp5;
        };
//        tmp5 = 9; // 编译错！对tmp5赋值导致它不是effectively final的
    }

    @Test
    public void example_1() {
        Stream<Double> stringStream = Stream.of(1.0, 4.0, 3.0, 2.0);
        int [] a = {2,3};
        stringStream.forEach((d)->System.out.println(a[4]));
        IntStream.iterate(2, i->i*2).limit(10).forEachOrdered(System.out::println);
    }

    @Test
    public void test1() {

    }
}
