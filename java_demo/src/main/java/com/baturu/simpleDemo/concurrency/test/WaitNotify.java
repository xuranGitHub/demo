package com.baturu.simpleDemo.concurrency.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/3/6.
 */
public class WaitNotify {

    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new WaitThread(), "WaitThread-name");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new NotifyThread(), "Notirythread-name");
        notifyThread.start();
    }

    static class WaitThread implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    System.out.println(Thread.currentThread() + " flag is true. wait@" +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println(this + " InterruptedException");
                    }
                }
                System.out.println(Thread.currentThread() + "flag is false. running@" +
                    new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class NotifyThread implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock. notify@" +
                    new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. sleep@" +
                    new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }
}
