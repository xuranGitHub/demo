package com.baturu.simpleDemo.concurrency.test;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/3/6.
 */
public class Join {

    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            previous = thread;
            thread.start();
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + " terminate.");
    }

    static class Domino implements Runnable {

        Thread previous;

        public Domino(Thread previous) {
            this.previous = previous;
        }

        @Override
        public void run() {
            try {
                previous.join();
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + "terminate.");
        }
    }
}
