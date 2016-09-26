package com.example.daiyiming.drawandguess.view;

import com.example.daiyiming.drawandguess.interfaces.OnCanvasUpdateListener;

/**
 * Created by daiyiming on 2016/9/13.
 */

public abstract class BaseCanvasHandler {

    protected Point mStartPoint = null;
    protected Point mEndPoint = null;
    private OnCanvasUpdateListener mListener = null;

    public BaseCanvasHandler(OnCanvasUpdateListener listener) {
        mListener = listener;
        mStartPoint = Point.createBlankPoint();
        mEndPoint = Point.createBlankPoint();
    }

    protected void callBack() {
        if (mListener != null) {
            mListener.updateCanvas(mStartPoint, mEndPoint);
        }
    }

}
