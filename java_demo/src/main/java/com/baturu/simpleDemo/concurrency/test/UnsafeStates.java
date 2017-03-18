package com.baturu.simpleDemo.concurrency.test;

/**
 * Created by xuran on 16/4/12.
 */
public class UnsafeStates {
    private String[] states = new String[] {
            "AK", "AL"
    };

    public String[] getStates() {
        return states;
    }
}
