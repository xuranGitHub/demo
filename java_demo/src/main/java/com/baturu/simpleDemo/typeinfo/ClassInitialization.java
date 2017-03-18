package com.baturu.simpleDemo.typeinfo;

import java.util.Random;

/**
 * Created by xuran on 16/7/6.
 */



class Initable {
    //编译期常量,它的值在编译期就可以确定;
    //对于运行时常量,它的值虽然在运行时初始化后不再发生变化，但问题就在于它的初始值要到运行时才能确定。
    static final int staticFinal = 47;
    static final int staticFinal2 = ClassInitialization.rand.nextInt(1000);
    static {
        System.out.println("Initializing Initable");
    }
}

class Initable2 {
    static int staticNonfinal = 147;
    static final int staticFinal2 = ClassInitialization.rand.nextInt(1000);
    static {
        System.out.println("Initializing Initable2");
    }
}

class Initable3 {
    static int staticNonFianl = 74;
    static {
        System.out.println("Initializing Initable3");
    }
}

public class ClassInitialization {

    public static Random rand = new Random(47);

    public static void main(String[] args) throws ClassNotFoundException {
//        Class initable = Initable.class;
        System.out.println("After createing Initable ref");
        System.out.println();
        System.out.println(Initable.staticFinal);
        System.out.println(Initable.staticFinal2);
        System.out.println();
        System.out.println(Initable2.staticNonfinal);
        System.out.println(Initable2.staticFinal2);
        Class initable3 = Class.forName("com.baturu.simpleDemo.typeinfo.Initable3");
        System.out.println("After creating Initable3 ref");
        System.out.println(Initable3.staticNonFianl);
    }
}


