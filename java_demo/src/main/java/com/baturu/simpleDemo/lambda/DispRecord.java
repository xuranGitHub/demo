package com.baturu.simpleDemo.lambda;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

/**
 * @authro xuran
 * @date 2016/1/26 23:44
 */
public class DispRecord {

    final String title;
    final int disp, length;
    DispRecord(String t, int d, int l) {
        this.title = t;
        this.disp = d;
        this.length = l;
    }

    int totalDisp() {
        return disp + length;
    }

    DispRecord(Book book) {
        this(book.getTitle(), 0, IntStream.of(book.getPageCounts()).sum());
    }

    public static Deque<DispRecord> wray(DispRecord dr) {
        Deque<DispRecord> ddr = new ArrayDeque<>();
        ddr.add(dr);
        return ddr;
    }
}
