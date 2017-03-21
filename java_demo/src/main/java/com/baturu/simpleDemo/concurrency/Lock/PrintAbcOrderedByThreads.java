package com.baturu.simpleDemo.concurrency.Lock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用Condition实现“三个线程循环打印ABC”，
 * Created by xuran on 2017/3/21.
 */
public class PrintAbcOrderedByThreads {

    private static final ReentrantLock reentrantLock = new ReentrantLock(false);
    private static final Condition mainCondition = reentrantLock.newCondition();

    public static void main(String[] args) {
        List<Condition> conditions = new LinkedList<>();
        Condition conditionA = reentrantLock.newCondition();
        conditions.add(conditionA);
        Condition conditionB = reentrantLock.newCondition();
        conditions.add(conditionB);
        Condition conditionC = reentrantLock.newCondition();
        conditions.add(conditionC);
        Thread threadA = new Thread(new PrintThread(conditionA), "A");
        Thread threadB = new Thread(new PrintThread(conditionB), "B");
        Thread threadC = new Thread(new PrintThread(conditionC), "C");
        threadA.start();
        threadB.start();
        threadC.start();
        try {
            int count = 0;
            Thread.sleep(1); //确保输出线程已经都进入等待状态
            reentrantLock.lock();
            while (count < 10) {
                for (Condition c : conditions) {
                    c.signal();
                    mainCondition.await();
                }
                count++;
            }
            System.out.println(reentrantLock.getHoldCount());
            System.out.println(reentrantLock.getQueueLength());
            System.out.println(reentrantLock.getWaitQueueLength(conditionA));
            System.out.println(reentrantLock.hasQueuedThread(threadA));
            System.out.println(reentrantLock.hasQueuedThreads());
            System.out.println(reentrantLock.hasWaiters(conditionA));
            System.out.println(reentrantLock.isFair());
            System.out.println(reentrantLock.isLocked());
            System.out.println(reentrantLock.isHeldByCurrentThread());
            threadA.interrupt();
            threadB.interrupt();
            threadC.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }

    }

    static class PrintThread implements Runnable {

        private Condition condition;

        public PrintThread(Condition condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " isHeldByCurrentThread " + reentrantLock.isHeldByCurrentThread());
            reentrantLock.lock();
            System.out.println(Thread.currentThread().getName() + " isHeldByCurrentThread " + reentrantLock.isHeldByCurrentThread());
            System.out.println(Thread.currentThread().getName() + ":get lock");
            try {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " will wait");
                    condition.await();
                    System.out.print(Thread.currentThread().getName());
                    mainCondition.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
