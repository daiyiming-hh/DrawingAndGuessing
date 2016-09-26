package com.example.daiyiming.drawandguess.asynctask;

import com.example.daiyiming.drawandguess.view.Point;

import java.util.List;

/**
 * Created by daiyiming on 2016/9/20.
 */

public class MessageHolder {
    public final int command;
    public final List<Point> points;
    public final String message;

    public MessageHolder(int command, List<Point> points, String message) {
        this.command = command;
        this.points = points;
        this.message = message;
    }
}
