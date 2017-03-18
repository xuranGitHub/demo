package com.baturu.simpleDemo.proxy;

/**
 * Created by xuran on 2015/12/18.
 */
public class RealSubject implements Subject {

    public void doSomething() {
        System.out.println("realSubject doSomething");
    }
}
