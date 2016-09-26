package com.example.daiyiming.drawandguess.view;

import android.view.MotionEvent;
import android.view.View;

import com.example.daiyiming.drawandguess.interfaces.IWriterDataSetter;
import com.example.daiyiming.drawandguess.interfaces.OnCanvasUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiyiming on 2016/9/13.
 */

public class TouchCanvasHandler extends BaseCanvasHandler implements View.OnTouchListener {

    private List<Point> mPointCache = new ArrayList<>();
    private IWriterDataSetter mDataSetter = null;

    public TouchCanvasHandler(OnCanvasUpdateListener listener, View view, IWriterDataSetter dataSetter) {
        super(listener);
        view.setOnTouchListener(this);
        mDataSetter = dataSetter;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mStartPoint.reset(x, y);
                mEndPoint.reset(x, y);
                mPointCache.add(Point.create(x, y));
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                mEndPoint.reset(x, y);
                mPointCache.add(Point.create(x, y));
                callBack();
                mStartPoint.reset(x, y);
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mPointCache.add(Point.createBlankPoint());
                if (mDataSetter != null) {
                    mDataSetter.setDrawData(mPointCache);
                }
                mPointCache.clear();
            }
            break;
        }
        return true;
    }

}
