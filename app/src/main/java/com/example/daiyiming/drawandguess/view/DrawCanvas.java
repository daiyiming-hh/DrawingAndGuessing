package com.example.daiyiming.drawandguess.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.daiyiming.drawandguess.interfaces.OnCanvasUpdateListener;

/**
 * Created by daiyiming on 2016/9/13.
 */

public class DrawCanvas extends SurfaceView implements SurfaceHolder.Callback, OnCanvasUpdateListener {

    private SurfaceHolder mHolder = null;
    private Bitmap mBufferBitmap = null;
    private Paint mPaint = null;
    private Point mStartPoint = null;
    private Point mEndPoint = null;

    public DrawCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHolder();
        initPaint(context);
        mStartPoint = Point.createBlankPoint();
        mEndPoint = Point.createBlankPoint();
    }

    private void initHolder() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                context.getResources().getDisplayMetrics()));
    }

    @Override
    public void updateCanvas(Point startPoint, Point endPoint) {
        mStartPoint.reset(startPoint.x, startPoint.y);
        mEndPoint.reset(endPoint.x, endPoint.y);
        draw();
    }

    public void clearCanvas() {
        if (isBitmapEnable()) {
            Canvas canvas = new Canvas(mBufferBitmap);
            canvas.drawColor(Color.WHITE);
            mStartPoint.resetBlank();
            mEndPoint.resetBlank();
            draw();
        }
    }

    private boolean isBitmapEnable() {
        return mBufferBitmap != null && !mBufferBitmap.isRecycled();
    }

    private void draw() {
        drawOnBitmap();
        drawOnCanvas();
    }

    private void drawOnBitmap() {
        if (isBitmapEnable() && !mStartPoint.isBlankPoint() && !mEndPoint.isBlankPoint()) {
            Canvas canvas = new Canvas(mBufferBitmap);
            canvas.drawLine(mStartPoint.x, mStartPoint.y, mEndPoint.x, mEndPoint.y, mPaint);
        }
    }

    private void drawOnCanvas() {
        Canvas canvas = mHolder.lockCanvas();
        if (isBitmapEnable()) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isBitmapEnable()) {
            mBufferBitmap.recycle();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mBufferBitmap == null) {
            mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(mBufferBitmap);
            canvas.drawColor(Color.WHITE);
        }
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

}
