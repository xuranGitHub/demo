package com.baturu.testCode;

/**
 * Created by xuran on 16/8/11.
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 模拟三个线程顺序交替执行
 * @author DELL
 *
 */
public class PrintThreadTest {
    class PrintThread1 implements Runnable{
        final ReentrantLock lock=new ReentrantLock();

        //三个条件用来控制三个线程,每次打印前判断如果前一个没有打印，则自己需要等待，自己打印完后需要唤醒后面的打印，同时更新打印标记
        final Condition con_a=lock.newCondition();
        final Condition con_b=lock.newCondition();
        final Condition con_c=lock.newCondition();

        //打印标记，用来记录上一次打印的变量，根据此标记来判断当前打印是否需要等待
        volatile String signal="c";

        final int count=10;

        void print_a(){
            try {
                lock.lock();
                if(!"c".equals(signal)){
                    con_a.await();
                }
                System.out.print("a");
                signal="a";
                con_b.signal();
            } catch (Exception e) {
            }finally{
                lock.unlock();
            }
        }

        void print_b(){
            try {
                lock.lock();
                if(!"a".equals(signal)){
                    con_b.await();
                }
                System.out.print("b");
                signal="b";
                con_c.signal();
            } catch (Exception e) {
            }finally{
                lock.unlock();
            }
        }

        void print_c(){
            try {
                lock.lock();
                if(!"b".equals(signal)){
                    con_c.await();
                }
                System.out.println("c");
                signal="c";
                con_a.signal();
            } catch (Exception e) {
            }finally{
                lock.unlock();
            }
        }

        public void run() {
            if("a".equals(Thread.currentThread().getName()))
                for(int i=0;i<count;i++){
                    print_a();
                }
            else if("b".equals(Thread.currentThread().getName()))
                for(int i=0;i<count;i++){
                    print_b();
                }
            else
                for(int i=0;i<count;i++){
                    print_c();
                }
        }
    }
    public static void main(String[] args) {
        PrintThreadTest pt=new PrintThreadTest();
        PrintThread1 pthread=pt.new PrintThread1();
        new Thread(pthread,"b").start();
        new Thread(pthread,"a").start();
        new Thread(pthread,"c").start();

    }
}