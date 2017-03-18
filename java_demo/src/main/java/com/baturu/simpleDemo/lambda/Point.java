package com.baturu.simpleDemo.lambda;

import lombok.Data;

/**
 * @authro xuran
 * @date 2016/1/26 22:38
 */
@Data
public class Point {

    public static final int MAX_DISTANCE = 2;

    private int x;

    public int distance(Point point) {
        return point.getX() > this.x ? point.getX() - x : x - point.getX();
    }
}
