package com.baturu.simpleDemo.concurrency.thread;

/**
 * Created by xuran on 2017/3/14.
 */
public class InterruptTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Thread thread1 = new Thread(){
            public void run(){
                try{
                    long time = System.currentTimeMillis();
                    while(System.currentTimeMillis()-time<2000){
                    }
                    System.out.println("A1");
                }
                catch(Exception e)
                {
                    System.out.println("B1");
                }
            }
        };
        thread1.start();
        thread1.interrupt();

        //在线程sleep状态下进行中断
        Thread thread2 = new Thread(){
            public void run(){
                try {
                    this.sleep(2000);
                    System.out.println("A2");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("B2");
                }
            }

        };

        thread2.start();
        thread2.interrupt();

        //在线程wait状态下进行中断,其中wait()没有在同步块中
        Thread thread3 = new Thread(){
            public void run(){
                try {
                    this.wait(2000);
                    System.out.println("A3");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("B3");
                }
            }

        };

        thread3.start();
        thread3.interrupt();

        //在线程wait状态下进行中断,其中wait()在同步块中
        Thread thread4 = new Thread(){
            public void run(){
                try {
                    synchronized(this){
                        this.wait(2000);
                        System.out.println("A4");}
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("B4");
                }
            }

        };

        thread4.start();
        thread4.interrupt();


        try{
            thread4.start();
            System.out.println("A5");
        }
        catch(Exception e)
        {
            System.out.println("B5");
            System.out.println(e.toString());
        }


    }

}
