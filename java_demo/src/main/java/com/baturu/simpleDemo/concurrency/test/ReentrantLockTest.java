package com.baturu.simpleDemo.concurrency.test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuran on 16/4/14.
 */
public class ReentrantLockTest {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方
    public static void main(String[] args)  {
        final ReentrantLockTest test = new ReentrantLockTest();

        new Thread("thread-1"){
            public void run() {
                test.insert2();
            }
        }.start();

        new Thread("thread-2"){
            public void run() {
                test.insert2();
            }
        }.start();
    }

    public void insert() {
        Thread thread = Thread.currentThread();
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }

    public void insert2() {
        Thread thread = Thread.currentThread();
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i=0;i<5;i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"排队人数较多,请稍后再试!");
        }
    }

    public void insert3() {
        Thread thread = Thread.currentThread();
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i=0;i<5;i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"排队人数较多,请稍后再试!");
        }
    }
}
