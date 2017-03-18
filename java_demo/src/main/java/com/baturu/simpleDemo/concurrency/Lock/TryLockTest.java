package com.baturu.simpleDemo.concurrency.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuran on 16/3/14.
 */
public class TryLockTest {
    public static void main(String[] args) {
        TryLockTest test = new TryLockTest();
        MyThread1 myThread11 = new MyThread1(test);
        MyThread1 myThread12 = new MyThread1(test);
        myThread11.start();
        myThread12.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myThread12.interrupt();
    }

    private Lock lock  = new ReentrantLock();
    public void insert(Thread thread) throws InterruptedException {
        lock.lock();
        try {
            System.out.println(thread.getName() + "获取了锁.");
        } finally {
            lock.unlock();
        }
    }
}

class MyThread1 extends Thread {
    private TryLockTest tryLockTest = null;
    public MyThread1(TryLockTest tryLockTest) {
        this.tryLockTest = tryLockTest;
    }

    @Override
    public void run() {
        try {
            tryLockTest.insert(Thread.currentThread());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"被中断");
        }
    }
}