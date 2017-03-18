package com.baturu.simpleDemo.concurrency.test;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuran on 16/3/6.
 */
public class Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaemonThread");
        thread.setDaemon(true);
        System.out.println("start--");
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        public void run() {
            System.out.println("run--");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("DaemonThread finally ran.");
            }
        }
    }
}
