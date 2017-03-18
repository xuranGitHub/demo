package com.baturu.simpleDemo.lambda;

import org.junit.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baturu.simpleDemo.lambda.Book.COMPUTING;
import static com.baturu.simpleDemo.lambda.Book.MEDICINE;
import static com.baturu.simpleDemo.lambda.Book.getBooks;

/**
 * @authro xuran
 * @date 2016/1/20 22:51
 */
public class Chapter2 {

    public static final List<Book> library = getBooks();

    @Test
    /* boolean test(T t) */
    public void testPredicate() {
        Predicate<Book> computingFilter = b -> b.getTopic() == COMPUTING;

        /*1*/
        Predicate<Book> notComputingFilter = computingFilter.negate();
        boolean computing = notComputingFilter.test(library.get(0));

        /*2*/
        Predicate<Book> computingAndMedicine = computingFilter.and(
                b -> b.getTopic() == MEDICINE);
        computingAndMedicine.test(library.get(0));

        /*3*/
        Predicate<Book> computingOrMedicine = computingFilter.or(
                b -> b.getTopic() == MEDICINE);
        computingOrMedicine.test(library.get(0));

        /*4*/
        System.out.println(computingAndMedicine.equals(computingOrMedicine));

        /*-*/
        Stream<Book> result = library.stream().filter(computingOrMedicine);
        System.out.println(result.collect(Collectors.toSet()));

    }

    @Test
    /* void accept(T t) */
    public void testConsumer() {
        Consumer<Book> printTitle = b -> System.out.println(b.getTitle());
        /*1*/
        Consumer<Book> printTitleThenPrintAuthors = printTitle.andThen(
                b -> System.out.println(b.getAuthors()));

        /*-*/
        library.forEach(printTitleThenPrintAuthors);
    }

    @Test
    /* T get() */
    public void testSupplier() {
        Supplier<Book> getBook =() -> Book.builder().build();
    }

    @Test
    /* U apply(T t) */
    public void testFunction() {
        Function<String, Book> getBookFromTitle = s -> Book.builder().title(s).build();
        Function<Book, Year> getYearOfBook = b -> b.getPubDate();
        /*1*/
        Function<String, Year> getYearOfTitleBoods = getYearOfBook.compose(getBookFromTitle);
        /*2*/
        Function<String, Year> getBoodsAndGetYear = getBookFromTitle.andThen(getYearOfBook);
    }

    @Test
    public void testSort() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(6);
        list.add(1);
        list.add(2);
        list = list.stream().sorted(Comparator.comparing(c -> c))
                .collect(Collectors.toList());
        System.out.println(list);
    }
}
