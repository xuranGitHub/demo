package com.baturu.simpleDemo.lambda;

import org.junit.Test;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.baturu.simpleDemo.lambda.Book.*;

/**
 * @authro xuran
 * @date 2016/1/20 21:47
 */
public class Chapter3 {

    public static final List<Book> library = getBooks();

    @Test
    public void test1() {
        //todo:原生流
    }

    @Test
    /*转换通道*/
    public void test2() {
        /*过滤*/
        Stream<Book> computingBoods = library.stream()
                .filter(b -> b.getTopic() == COMPUTING);
        /*映射*/
        Stream<String> bookTitles = library.stream()
                .map(Book::getTitle);
/*
        IntStream mapToInt(ToIntFunction<T> f)
        LongStream mapToLong(ToLongFunction<T> f)
        DoubleStream mapToDouble(ToDoubleFunction<T> f)
*/
        Stream<String> authorsInBookTitleOrder = library.stream()
                .flatMap(book -> book.getAuthors().stream());

        /*flatMapToInt, flatMapToLong, flatMapToDouble*/

        /*调试*/
        List<Book> library2 = library.stream()
                .filter(b -> b.getTopic() == COMPUTING)
                .peek(b -> System.out.println(b))
                .filter(b -> b.getPubDate().isBefore(Year.of(2010)))
                .collect(Collectors.toList());

        /*排序*/
        Stream<Book> booksSortedByTitle = library.stream()
                .sorted(Comparator.comparing(Book::getTitle));

        /*去重*/
        Stream<String> distincTitles = library.stream()
                .map(Book::getTitle)
                .distinct();

        /*截断*/
        Stream<Book> readingList = library.stream()
                .skip(100)/*去除前100个*/
                .limit(100)/*选择前100个*/;


        Optional<Book> oldest = library.stream()
                .min(Comparator.comparing(Book::getPubDate));
        Set<String> titles = library.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
    }

    @Test
    /*终止通道*/
    public void test3() {
        /*搜索*/
        boolean allComputingBook = library.stream()
                .allMatch(b -> b.getTopic() == COMPUTING);
        boolean hasComputingBook = library.stream()
                .anyMatch(b -> b.getTopic() == COMPUTING);
        boolean noComputingBook = library.stream()
                .noneMatch(b -> b.getTopic() == COMPUTING);

        /*找到第一个，单线程*/
        Optional<Book> firstComputingBook = library.stream()
                .filter(b -> b.getTopic() == COMPUTING)
                .findFirst();
        /*找到任何一个， 多线程*/
        Optional<Book> anyComputingBook = library.stream()
                .filter(b -> b.getTopic() == COMPUTING)
                .findAny();
        /*      Optional
        T get() 没有时抛NoSuchElementException
        void ifPresent(Consumer<T>) 如果存在就传参执行
        boolean isPresent 如果存在就返回true
        T orElse(T) 有则返回没有则返回参数
        T orElseGet(Supplier<T>) 有则返回没有则执行supplier返回结果
        */

        /*汇聚*/
        IntSummaryStatistics pageCountStatistics = library.stream()
                .mapToInt(b -> IntStream.of(b.getPageCounts()).sum())
                .summaryStatistics();
        /*  IntStream
            int sum()
            long count()
            OptionalInt min()
            OptionalInt max()
            OptionalDouble average()
            IntSummaryStatistics summaryStatistics()  综合上面五个结果
        */
        /*  Stream
            min(Comparator<T>) Optional<T>
            max(Comparator<T>) Optional<T>
        */

        /*副作用操作*/
        /*多线程*/
        library.forEach(b -> System.out.println(b));
        /*单线程*/
        library.stream().forEachOrdered(b -> System.out.println(b));
    }

    @Test
    public void testDouble() {
        for(double x = 0D;  x != 0.3D; x += 0.1D)
        {
            System.out.println(x);
            try
            {
                Thread.sleep(500L);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testString() {
        List<String> s = new ArrayList<>();
        s.add("1");
        s.add("2");
    }
}
