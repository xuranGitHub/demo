package com.baturu.simpleDemo.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xuran on 2015/12/18.
 */
public class Client {
    public static void main(String[] args) {
        List<String> values = new ArrayList<>();
        values.add("aa");
        values = fileter(values);
        System.out.println(values);
//        javaProxy();
//        cglibProxy();
//        cglibProxy2();
    }

    public static void javaProxy() {
        //    我们要代理的真实对象
        Subject realSubject = new RealSubject();

        //    我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new ProxyHandler(realSubject);

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        Subject subject = (Subject) Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);

        subject.doSomething();

        System.out.println(subject.getClass().getName());
    }

    public static void cglibProxy() {
        CglibProxy cglibProxy = new CglibProxy();
        RealSubjectNoInterface realSubject = (RealSubjectNoInterface) cglibProxy.getInstance(new RealSubjectNoInterface());
        realSubject.doSomething();
    }

    public static void cglibProxy2() {
        CglibProxy cglibProxy = new CglibProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubjectNoInterface.class);
        // 回调方法
        enhancer.setCallback(cglibProxy);
        // 创建代理对象
        RealSubjectNoInterface realSubject = (RealSubjectNoInterface) enhancer.create();
        realSubject.doSomething();
    }

    public static List<String> fileter(List<String> strs) {
        if (strs.size() <= 1) {
            strs = Collections.EMPTY_LIST;
            return strs;
        }
        String str = strs.iterator().next();
        strs.remove(str);
        return strs;
    }
}
