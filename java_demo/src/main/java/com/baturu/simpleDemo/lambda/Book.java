package com.baturu.simpleDemo.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

/**
 * @authro xuran
 * @date 2016/1/20 21:22
 */
@Data
@Builder
@AllArgsConstructor
public class Book {

    public static final Topic MEDICINE = Topic.builder().title("medicine").build();
    public static final Topic COMPUTING = Topic.builder().title("computing").build();
    public static final Topic FICTION = Topic.builder().title("fiction").build();

    public static List<Book> getBooks() {
        Book nails = new Book("Foundamentals of Chinese Fingernail Image",
                Arrays.asList("Li", "Fu", "Li"),
                new int[]{256},
                Year.of(2014),
                25.2,
                MEDICINE);

        Book dragon = new Book("Compilers: Principles, Techniques and Tools",
                Arrays.asList("Aho", "Lam", "Sethi", "Ullman"),
                new int[]{1009},
                Year.of(2006),
                23.6,
                COMPUTING);

        Book lotr = new Book("Lord of the Rings",
                Arrays.asList("Tolkien"),
                new int[]{531, 416, 624},
                Year.of(1955),
                23.0,
                FICTION);

        Book voss = new Book("Voss",
                Arrays.asList("Patrick White"),
                new int[]{478},
                Year.of(1957),
                19.8,
                FICTION);

        List<Book> library = Arrays.asList(nails, dragon, lotr, voss);
        return library;
    }

    private String title;

    private List<String> authors;

    private int[] pageCounts;

    private Year pubDate;

    private double height;

    private Topic topic;

}
