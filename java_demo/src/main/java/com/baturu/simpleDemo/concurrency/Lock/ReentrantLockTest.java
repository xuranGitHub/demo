package com.baturu.simpleDemo.concurrency.Lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuran on 2017/3/13.
 */
public class ReentrantLockTest {

    private ReentrantLock myLock = new ReentrantLock();

    private Condition condition = myLock.newCondition();

    private List<Integer> listBuffer = new ArrayList<Integer>();

    private volatile boolean runFlag = true;

    /**
     * 生产者 生产数据
     */
    public void produce() {
        int i = 0;
        while(runFlag) {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + " get the lock");
            try {
                // 生产者检查容器中是否有数据，如果容器中有数据则生产者等待
                // 如果容器中没有数据则生产数据放入容器中并通知消费者
                if (listBuffer.size() > 0) {
                    try {
                        // 调用await()方法，生产者线程阻塞并释放锁，之后进入该条件的等待集中
                        // 直到消费者调用signalAll()方法之后，生产者线程解除阻塞并重新竞争锁
                        // 生产者线程获得锁之后，重新开始从被阻塞的地方继续执行程序
                        System.out.println(Thread.currentThread().getName() + " produce will wait");
                        condition.await();
                        System.out.println(Thread.currentThread().getName() + " produce wake up");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " add Integer");
                    listBuffer.add(i++);
                    // 生产者线程调用signalAll()方法，通知消费者线程容器中有数据
                    condition.signalAll();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " produce will unlock");
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + " produce had unlocked");
            }
        }
    }

    /**
     * 消费者 读取数据
     */
    public void consume() {
        while(runFlag) {
            myLock.lock();
            System.out.println(Thread.currentThread().getName() + " get the lock");
            try {
                // 消费者检查容器中是否有数据，如果没有数据消费者等待
                // 如果容器中有数据则读取数据，读完之后通知生产者
                if (listBuffer.size() == 0) {
                    try {
                        // 同生产者线程一样，消费者线程调用await()方法阻塞并进入该条件等待集中
                        System.out.println(Thread.currentThread().getName() + " consume will wait");
                        condition.await();
                        System.out.println(Thread.currentThread().getName() + " consume wake up");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " get Integer");
                    long beginTime = 0;
                    System.out.println(listBuffer.remove(0));
                    beginTime = System.currentTimeMillis();
                    while(System.currentTimeMillis() - beginTime < 100) {}
                    // 消费者线程调用signalAll()方法，通知生产者生产数据
                    condition.signalAll();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " consume will unlock");
                myLock.unlock();
                System.out.println(Thread.currentThread().getName() + " consume had unlocked");
            }
        }
    }



    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final ReentrantLockTest test = new ReentrantLockTest();

        Thread produce = new Thread(new Runnable() {
            public void run() {
                test.produce();
            }
        },"A");

        Thread consume = new Thread(new Runnable() {
            public void run() {
                test.consume();
            }
        },"B");

        produce.start();
        consume.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        test.setRunFlag(false);
    }
}
