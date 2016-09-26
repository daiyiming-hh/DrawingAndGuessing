package com.example.daiyiming.drawandguess.view;

/**
 * Created by daiyiming on 2016/9/20.
 */

public class Point {

    public float x = 0;
    public float y = 0;

    private Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void reset(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void resetBlank() {
        reset(-1, -1);
    }

    public boolean isBlankPoint() {
        return !(x >= 0 && y >= 0);
    }

    public static Point create(float x, float y) {
        return new Point(x, y);
    }

    public static Point createBlankPoint() {
        return new Point(-1, -1);
    }

}
