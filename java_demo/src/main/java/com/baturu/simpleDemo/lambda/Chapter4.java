package com.baturu.simpleDemo.lambda;

import org.junit.Test;

import java.math.BigInteger;
import java.time.Year;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @authro xuran
 * @date 2016/1/20 23:33
 */
public class Chapter4 {

    private static final List<Book> library = Book.getBooks();

    @Test
    /*预定义收集器与组合收集器*/
    public void test1() {
        /*Collectors.joining()*/
        String concatenatedTitles = library.stream()
                .map(Book::getTitle)
                //.collect(joining());
                .collect(joining(","));
        List<String> titleToAuthors = library.stream()
                .map(b -> b.getAuthors().stream().collect(joining(",", b.getTitle() + ":", " write it")))
                .collect(toList());
        /*Collectors.toList(), toMap(), toCollection(), counting(), maxBy()*/
        Map<String, Year> titleToPubDate = library.stream()
                .collect(toMap(Book::getTitle, Book::getPubDate, (x, y) -> x.isAfter(y) ? x : y, TreeMap::new));
        Map<String, Year> titleToPubDate2 = library.stream()
                .sorted(Comparator.comparing(Book::getTitle).reversed())
                .collect(toMap(Book::getTitle, Book::getPubDate,
                        BinaryOperator.maxBy(Comparator.naturalOrder()), LinkedHashMap::new));
        NavigableSet<String> sortedTitles = library.stream()
                .map(Book::getTitle)
                .collect(toCollection(TreeSet::new));
        BlockingQueue<Book> queueInPubDateOrder = library.stream()
                .sorted(Comparator.comparing(Book::getPubDate))
                .collect(toCollection(LinkedBlockingDeque::new));
        long count = library.stream().collect(counting());
        Optional<Book> bookWithMaxAuthors = library.stream().collect(maxBy(Comparator.comparing(b -> b.getAuthors().size())));
        /*Collectors.groupingBy(Function<T,K>)*/
        Map<Topic, List<Book>> topicBooks = library.stream()
                .collect(groupingBy(Book::getTopic/*, Collectors.toList()*/));
        /*Collectors.partitioningBy*/
        Map<Boolean, List<Book>> computingBooksMap = library.stream()
                .collect(partitioningBy(b -> b.getTopic() == Book.COMPUTING));
        /*groupingBy延伸， 组合收集器*/
        Map<Topic, Long> topicBooksCountMap = library.stream()
                .collect(groupingBy(Book::getTopic, counting()));
        Map<Topic, Optional<Book>> topicBookWithMaxAuthor = library.stream()
                .collect(groupingBy(Book::getTopic,
                        maxBy(Comparator.comparing(b -> b.getAuthors().size()))));
        Map<Topic, Integer> topicAuthorsNumMap = library.stream()
                .collect(groupingBy(Book::getTopic,
                        summingInt(b -> b.getPageCounts().length)));
        Map<Topic, Double> topicBookAverage = library.stream()
                .collect(groupingBy(Book::getTopic, averagingDouble(b -> b.getHeight())));
        Map<Topic, IntSummaryStatistics> topicBookISS = library.stream()
                .collect(groupingBy(Book::getTopic, summarizingInt(b -> b.getAuthors().size())));
        Map<Topic, List<String>> concatenatedTitlesByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        mapping(Book::getTitle, toList())));
        Map<Topic, String> concatenatedTitlesStrByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        mapping(Book::getTitle, joining(","))));
    }

    @Test
    public void test() {
        System.out.println(library.stream().collect(counting()));
        System.out.println(library.stream().collect(maxBy(Comparator.comparing(book -> book.getAuthors().size()))));
    }

    @Test
    /*链接管道 entrySet*/
    public void test2() {
        Map<Topic, Long> bookCountByTopic = library.stream()
                .collect(groupingBy(Book::getTopic, counting()));
        Stream<Map.Entry<Topic, Long>> entries = library.stream()
                .collect(groupingBy(Book::getTopic, counting())).entrySet().stream();
        Optional<Topic> mostPopularTopic = entries
//                .max(Comparator.comparing(e -> e.getValue()))
//                .map(e -> e.getKey());
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        Optional<Topic> mostPopularTopic2 = library.stream()
                .collect(groupingBy(Book::getTopic, counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        /*可能会有多个主题的书本数一样，终极版*/
        Optional<Set<Topic>> mostPopularTopics = library.stream()
                .collect(groupingBy(Book::getTopic, counting()))
                .entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toSet())))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue);
    }

    @Test
    /**
     * 自定义 收集器（提供器， 积聚器，合并器） ，完成器
     * 并发计算List<T>过程中，一旦需要根据前者某些状态才能完成时，需要用到收集器
     * 创建集合，收集数据，合并集合的异步过程，这是我的理解
     */
    public void test3() {
        List<Point> sortedPointList = new ArrayList<>();
        Deque<Deque<Point>> displacementRecords = sortedPointList.stream()
                .collect(Collector.of(supplier, accumulator, combiner));
        List<String> titles = library.stream()
                .map(Book::getTitle)
                /*后面一部分即完成器*/
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

        /*计算书架从左往右的当前书的位置 收集器+完成器 */
        Supplier<Deque<DispRecord>> bookSupplier = ArrayDeque::new;
        BiConsumer<Deque<DispRecord>, Book> bookAccumulator =
                (dpLeft, b) -> {
                    int disp = dpLeft.isEmpty() ? 0 : dpLeft.getLast().totalDisp();
                    dpLeft.add(new DispRecord(b.getTitle(), disp,
                            IntStream.of(b.getPageCounts()).sum()));
                };
        BinaryOperator<Deque<DispRecord>> bookCombiner =
                (dpLeft, dpRight) -> {
                    if (dpLeft.isEmpty()) return dpRight;
                    int disp = dpLeft.getLast().totalDisp();
                    List<DispRecord> dispRecords = dpRight.stream()
                            .map(d -> new DispRecord(d.title, d.disp + disp, d.length))
                            .collect(toList());
                    dpLeft.addAll(dispRecords);
                    return dpLeft;
                };
        Function<Deque<DispRecord>, Map<String, Integer>> bookFinisher =
                ddr -> ddr.parallelStream().collect(
                            toConcurrentMap(dr -> dr.title, dr -> dr.disp));
        /*一个调用收集器+完成器的例子*/
        Map<String, Integer> bookPosition = library.stream()
                .collect(Collector.of(bookSupplier, bookAccumulator, bookCombiner, bookFinisher));
    }

    /*不用收集器的原始代码*/
    Deque<Deque<Point>> groupByProximity(List<Point> sortedPointList) {
        Deque<Deque<Point>> points = new ArrayDeque<>();
        points.add(new ArrayDeque<>());
        for (Point p : sortedPointList) {
            Deque<Point> lastSegment = points.getLast();
            if (!lastSegment.isEmpty() &&
                    lastSegment.getLast().distance(p) > Point.MAX_DISTANCE) {
                Deque<Point> newSegment = new ArrayDeque<>();
                newSegment.add(p);
                points.add(newSegment);
            } {
                lastSegment.add(p);
            }
        }
        return points;
    }

    /*提供器*/
    Supplier<Deque<Deque<Point>>> supplier =
            () -> {
                Deque<Deque<Point>> points = new ArrayDeque<>();
                points.add(new ArrayDeque<>());
                return points;
            };

    /*积聚器*/
    BiConsumer<Deque<Deque<Point>>, Point> accumulator =
            (ddp, p) -> {
                Deque<Point> lastSegment = ddp.getLast();
                if (!lastSegment.isEmpty() &&
                        lastSegment.getLast().distance(p) > Point.MAX_DISTANCE) {
                    Deque<Point> newSegment = new ArrayDeque<>();
                    newSegment.add(p);
                    ddp.add(newSegment);
                } {
                    lastSegment.add(p);
                }
            };

    /*合并器*/
    BinaryOperator<Deque<Deque<Point>>> combiner =
            (left, right) -> {
                Deque<Point> lastLeftDeque = left.getLast();
                if (lastLeftDeque.isEmpty()) return right;
                Deque<Point> firstRightDeque = right.getFirst();
                if (firstRightDeque.isEmpty()) return left;
                if (lastLeftDeque.getLast().distance(firstRightDeque.getFirst())
                        <= Point.MAX_DISTANCE) {
                    lastLeftDeque.add(firstRightDeque.getFirst());
                    firstRightDeque.remove(firstRightDeque.getFirst());
                }
                left.addAll(right);
                return left;
            };

    @Test
    /**
     * 汇聚, 初始值，组合处理， 返回结果
     */
    public void test4() {
        int[] array = new int[]{1, 2, 3};
        int sum = Arrays.stream(array).sum();

        int sum1 = IntStream.of(1, 2, 3).sum();

        /*sum, count, average都是由此计算而来*/
        int sum2 = IntStream.of(1, 2, 3).reduce(0, (a, b) -> a + b);

        /*计算阶乘*/
        int intArg = 5;/*  1*2*3*4*5=120 */
        int intArgFactorial = IntStream
                .rangeClosed(1, intArg)
                .reduce(1, (a, b) -> a * b);
        System.out.println(intArgFactorial);

        /*找出最小值*/
        OptionalInt min = IntStream.of(6, 2, 9)
                .reduce((a, b) -> Math.min(a, b));
        System.out.println(min);

        /*找出书名最靠前的书*/
        Comparator<Book> titleComparator =
                Comparator.comparing(Book::getTitle);
        Optional<Book> first = library.stream()
                .reduce(BinaryOperator.minBy(titleComparator));

        /* BigInteger求和 */
        Stream<BigInteger> biStream = LongStream.of(1, 2, 3)
                .mapToObj(BigInteger::valueOf);
        Optional<BigInteger> bigIntegerSum = biStream
                .reduce(BigInteger::add);

        /* 计算书籍的总卷数 todo:*/
        int totalVolumes = library.stream()
              //.mapToInt(b -> b.getPageCounts().length).sum()
                /*此重载方法引入了积聚器*/
                .reduce(0, (count, book) -> book.getPageCounts().length + count, Integer::sum);
        System.out.println(totalVolumes);

        /* todo：此处好好学习 */
        BinaryOperator<Deque<DispRecord>> bookCombiner =
                (dpLeft, dpRight) -> {
                    if (dpLeft.isEmpty()) return dpRight;
                    int disp = dpLeft.getLast().totalDisp();
                    List<DispRecord> dispRecords = dpRight.stream()
                            .map(d -> new DispRecord(d.title, d.disp + disp, d.length))
                            .collect(toList());
                    dpLeft.addAll(dispRecords);
                    return dpLeft;
                };
        Map<String, Integer> displacementMap = library.stream()
                .map(DispRecord::new)
                .map(DispRecord::wray)
                .reduce(bookCombiner).orElseGet(ArrayDeque::new)
                .stream()
                .collect(toMap(dr -> dr.title, dr -> dr.disp));

        /* 每个专题中最厚的书 todo: */
        Comparator<Book> htComparator =
                Comparator.comparing(Book::getHeight);
        Map<Topic, Optional<Book>> maxHeightByTopic = library.stream()
                .collect(groupingBy(Book::getTopic, reducing(BinaryOperator.maxBy(htComparator))));

        /* 查询每一主题下卷宗的数量*/
        Map<Topic, Integer> volumeByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        reducing(0, b -> b.getPageCounts().length, Integer::sum)));
    }

    @Test
    public void testDate() {
        Date time = new Date();
        System.out.println(time);
        Long i = 1L;
        System.out.println(i == 1);
    }
}
