package com.baturu.simpleDemo.concurrency.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuran on 16/2/20.
 */
public class Counter {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int i = 0;
    public static void main(String[] args) {
        final Counter cas = new Counter();
        List<Thread> ts = new ArrayList<>(600);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    cas.count();
                    cas.safeCount();
                }
            });
            ts.add(t);
        }
        ts.stream().forEach(thread -> thread.start());
        ts.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(cas.i);
        System.out.println(cas.atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 非线程安全计数器
     */
    private void count() {
        i++;
/*
        synchronized (this) {
            i++;
        }
*/
    }

    /**
     * 使用cas实现线程安全计数器
     */
    private void safeCount() {
        for (;;) {
            int i = atomicInteger.get();
            boolean suc = atomicInteger.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }
}
