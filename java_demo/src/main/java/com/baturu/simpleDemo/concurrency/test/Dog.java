package com.baturu.simpleDemo.concurrency.test;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/4/12.
 */
public class Dog {

    public Dog() {
        System.out.println("new Dog");
    }

    public Dog(String name) {
        System.out.println("new Dog: " + name);
    }

    public void say() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " , dog say");
    }

    public synchronized void say2() {
        System.out.println(Thread.currentThread().getName() + " , dog synchronized say2");
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
