package com.example.daiyiming.drawandguess.view;

import com.example.daiyiming.drawandguess.interfaces.OnCanvasUpdateListener;

/**
 * Created by daiyiming on 2016/9/14.
 */

public class SocketCanvasHandler extends BaseCanvasHandler {

    public SocketCanvasHandler(OnCanvasUpdateListener listener) {
        super(listener);
    }

    public void addLine(Point start, Point end) {
        mStartPoint.reset(start.x, start.y);
        mEndPoint.reset(end.x, end.y);
        callBack();
    }

}
