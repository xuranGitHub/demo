package com.baturu.testCode;

/**
 * Created by xuran on 16/8/5.
 */
public class LockTest {

    static String str = "a";

    public static void main(String[] args) {

        Thread a = new Thread(() -> {
            for (int i=0; i <=10; ) {
                synchronized (LockTest.class) {
                    if (!str.equals("a")) {
                        try {
                            System.out.println("a will wait()");
                            LockTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("a");
                        i++;
                        str = "b";
                        LockTest.class.notifyAll();
                    }
                }
            }
        });

        Thread b = new Thread(() -> {
            for (int i=0; i <=10; ) {
                synchronized (LockTest.class) {
                    if (!str.equals("b")) {
                        try {
                            System.out.println("b will wait()");
                            LockTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("b");
                        i++;
                        str = "c";
                        LockTest.class.notifyAll();
                    }
                }
            }
        });

        Thread c = new Thread(() -> {
            for (int i=0; i <=10; ) {
                synchronized (LockTest.class) {
                    if (!str.equals("c")) {
                        try {
                            System.out.println("c will wait()");
                            LockTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("c");
                        i++;
                        str = "a";
                        LockTest.class.notifyAll();
                    }
                }
            }
        });

        b.start();
        c.start();
        a.start();
    }
}

