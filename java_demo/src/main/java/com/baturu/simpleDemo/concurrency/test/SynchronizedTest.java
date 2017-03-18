package com.baturu.simpleDemo.concurrency.test;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/4/12.
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        SuperDog superDog = new SuperDog();

        Thread a = new Thread(() -> {
            superDog.fly();
        }, "a");
        Thread d = new Thread(() -> {
            superDog.fly2();
        }, "d");
        System.out.println(superDog.getA());
        d.start();
        a.start();
        System.out.println(superDog.getA());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(superDog.getA());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(superDog.getA());
    }
}


