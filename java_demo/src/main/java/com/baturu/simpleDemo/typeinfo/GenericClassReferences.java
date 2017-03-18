package com.baturu.simpleDemo.typeinfo;

/**
 * 普通的类引用不会产生警告信息,你可以看到,尽管范型类引用只能赋值为指向其声明的类型,
 * 但是普通的类引用可以呗重新赋值为指向任何其他的Class对象.
 * Created by xuran on 16/7/6.
 */
public class GenericClassReferences {
    public static void main(String[] args) {
        Class intClass = int.class;
        Class<Integer> genericIntClass = int.class;
        genericIntClass = Integer.class;
        intClass = double.class;
        // genericIntClass = double.class; // Illegal

        Class<?> oneClass = int.class;
        oneClass = double.class;
    }
}
