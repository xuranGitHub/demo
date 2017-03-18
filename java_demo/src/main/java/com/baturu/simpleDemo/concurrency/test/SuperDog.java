package com.baturu.simpleDemo.concurrency.test;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/4/12.
 */
public class SuperDog extends Dog {

    int a = 1;

    public SuperDog() {
        super("this");
        System.out.println("new SuperDog");
        System.out.println(super.toString());
        System.out.println(this.toString());
    }

    public void fly() {
        System.out.println(Thread.currentThread().getName() + " , dog fly");
        a = 10;
    }

    public synchronized void fly2() {
        System.out.println(Thread.currentThread().getName() + " , dog synchronized fly2");
        try {
            a = 0;
            TimeUnit.SECONDS.sleep(2);
            a = 3;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return 2;
    }

    public int getA() {
        return a;
    }
}
