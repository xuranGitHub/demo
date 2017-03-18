package com.baturu.testCode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by xuran on 16/8/11.
 */
public class RandomTest{
    public static void main(String[] args) {
        int[] num = new int[15];
        for (int i = 0; i < 15; i++) {
            num[i] = i;
        }
        Integer[] result = getRandomNum(num, 10);
        System.out.println(Arrays.toString(result));
    }

    public static Integer[] getRandomNum(int[] num, int n) {
        Set<Integer> sets = new HashSet<Integer>();
        Random random = new Random();
        while (sets.size() < n) {
            sets.add(random.nextInt(num.length));
        }
        return sets.toArray(new Integer[n]);
    }
}
