package com.baturu.simpleDemo.concurrency.thread;

/**
 * Created by xuran on 2017/3/10.
 */
public class OutPrint extends Thread{

    /**
     * @param args
     */
    private final static int maxCount=10;
    private static boolean[] bool={true,false,false};
    private int tag;
    private int count;
    public OutPrint(String ot,int tag){
        super(ot);
        this.tag=tag;
    }
    public static void main(String[] args) throws InterruptedException {

        Thread threadA=new OutPrint("A",0);
        Thread threadB=new OutPrint("B",1);
        Thread threadC=new OutPrint("C",2);

        long begin=System.currentTimeMillis();
        threadA.start();

        threadB.start();

        threadC.start();

        threadA.join();

        threadB.join();

        threadC.join();

        long end=System.currentTimeMillis();

        System.out.println();
        System.out.println("耗时： "+(end-begin)+"ms");
    }

    public void run(){
        try{
            synchronized (bool) {
                while(true){
                    if(bool[this.tag]){
                        System.out.print(Thread.currentThread().getName());
                        this.count++;
                        bool[this.tag]=false;
                        bool[(this.tag+1)%3]=true;
                        bool[(this.tag+2)%3]=false;
                        bool.notifyAll();
                        if(count==maxCount)
                            return;

                    }else{
                        bool.wait();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
