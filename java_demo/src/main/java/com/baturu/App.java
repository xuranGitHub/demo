package com.baturu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void test( String[] args )
    {
        System.out.println( "Hello World!" );
        new Thread(()-> System.out.println("hello")).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }).start();

        List<String> array = new ArrayList<String>();
        array.add("a");
        array.add("b");
        array.add("c");
        array.forEach(System.out::println);


    }

    public static void main(String[] args) {
        Date date = new Date(1462421759202L);
        System.out.println(date);
        List<String> removeFieldList = new ArrayList<>();
        removeFieldList.add("1");
        removeFieldList.add("2");
        deleteByFields("key", removeFieldList);
    }

    public static Long deleteByFields(String key, List<String> fields) {
        if (fields == null || fields.size() == 0) {
            return new Long(0);
        }
        String[] s = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            s[i] = fields.get(i);
        }
        testA(s);
        System.out.println(fields);
        return 1L;
    }

    public static void testA(final String... fields) {
        System.out.println(fields.toString());
        return;
    }
}
