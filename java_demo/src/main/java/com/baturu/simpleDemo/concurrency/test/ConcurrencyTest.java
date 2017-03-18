package com.baturu.simpleDemo.concurrency.test;

/**
 * Created by xuran on 16/2/20.
 * 可修改count值,会发现,数量级较小时,并发比串行慢,达到一定数量级并发的才会优于串行,因为并发的上下文切换会带来时间上的消耗
 */
public class ConcurrencyTest {

    private static final long count = 1000000001L;

    public static void main(String[] args) throws InterruptedException {
        concurrency();
        serial();
    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (long i = 0; i < count; i++) {
                a += 5;
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency :" + time + "ms, b = " + b);
    }

    private static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial :" + time + "ms, a = " + a + ", b = " + b);
    }
}
