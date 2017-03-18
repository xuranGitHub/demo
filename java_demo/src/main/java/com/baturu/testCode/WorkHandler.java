package com.baturu.testCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/12/18.
 */
public class WorkHandler implements InvocationHandler {

    private Object proxied;

    public WorkHandler(Object proxied) {
        this.proxied = proxied;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //　　在代理真实对象前我们可以添加一些自己的操作
        System.out.println("before worker work");

        System.out.println("Method:" + method);

        //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        System.out.println("do it");
        Object result = method.invoke(proxied, args);

        //　　在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("after worker work");

        return result;
    }
}
