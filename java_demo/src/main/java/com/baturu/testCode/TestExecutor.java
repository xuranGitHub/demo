package com.baturu.testCode;

/**
 * Created by xuran on 2016/11/21.
 */
public class TestExecutor {


    public static final int TRY_TIMES = 1;

    public static void main(String[] args) {
//        // 创建一个可重用固定线程数的线程池
//        try {
//
//        //        doA(TRY_TIMES);
//            for (Integer i = 0; i <= 5000; i++) {
//                ExecutorService pool = Executors.newSingleThreadExecutor();
//                // 创建线程
//                final Integer finalI = i;
//                Callable<String> task = () -> {
//                    System.out.println("run:" + finalI);
//                    Thread.currentThread().sleep(2000);
//                    System.out.println();
//                    return "";
//                };
//                //将线程放入池中进行执行
//                Future<String> future = pool.submit(task);
//                System.out.println("submit'" + i.toString() + "' into pool");
//                System.out.println(future.isDone());
//                //pool.shutdown();
//            }
//            // 关闭线程池
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        System.out.println("1" + TRY_TIMES);
        doDecrease(TRY_TIMES);
        System.out.println("5" + TRY_TIMES);
    }

    public static void doDecrease(int tryTimes) {
        System.out.println("2" + tryTimes);
        if (--tryTimes > 0) {
            System.out.println("3" + tryTimes);
        }
        System.out.println("4" + tryTimes);
    }

    private static void doA(int a) {
        System.out.println(a);
        System.out.println(--a);
        System.out.println();
    }
}

class MyThread extends Thread {

    String name;

    MyThread(String name) {
        this.name = name ;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ":'" + name + "'正在执行。。。");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
