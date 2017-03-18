package com.baturu.testCode;

/**
 * Created by xuran on 16/10/9.
 */
public class IntegerEquals {
    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        System.out.println(a==b);
        Integer c = 128;
        Integer d = 128;
        System.out.println(c==d);

        String A = new String("a");
        String B = new String("a");
        System.out.println(A==B);

    }
}
