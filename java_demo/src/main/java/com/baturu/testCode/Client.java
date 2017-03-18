package com.baturu.testCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2015/12/18.
 */
public class Client {

    public static void main(String[] args) {
        Workable worker = new Worker();
        InvocationHandler workHandler = new WorkHandler(worker);
        Workable workable = (Workable) Proxy.newProxyInstance(workHandler.getClass().getClassLoader(), worker
                .getClass().getInterfaces(), workHandler);
        int num = workable.work();
        System.out.println(num);
        System.out.println(workable.getClass().getName());
    }
}
