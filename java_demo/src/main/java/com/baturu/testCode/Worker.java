package com.baturu.testCode;

/**
 * Created by Administrator on 2015/12/18.
 */
public class Worker implements Workable {
    public int work() {
        System.out.println("worker is working!");
        return 8;
    }
}
