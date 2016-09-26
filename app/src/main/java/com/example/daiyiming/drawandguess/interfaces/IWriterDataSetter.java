package com.example.daiyiming.drawandguess.interfaces;

import com.example.daiyiming.drawandguess.view.Point;

import java.util.List;

public interface IWriterDataSetter {
    void setDrawData(List<Point> points);
    void setMessageData(String message);
    void setClearData();
}