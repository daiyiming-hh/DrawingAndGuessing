package com.example.daiyiming.drawandguess.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.daiyiming.drawandguess.interfaces.OnMessageReceiveListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by daiyiming on 2016/9/14.
 */

public class ReaderAsyncTask extends AsyncTask<Void, Void, Void> {

    private volatile boolean mIsRunning = true;
    private BufferedReader mBufferReader = null;
    private OnMessageReceiveListener mListener = null;

    public ReaderAsyncTask(InputStream inputStream, OnMessageReceiveListener listener) {
        mBufferReader = new BufferedReader(new InputStreamReader(inputStream));
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void[] objects) {
        while (mIsRunning) {
            try {
                String data = mBufferReader.readLine();
                if (!TextUtils.isEmpty(data)) { // 如果为空则直接丢弃此次数据
                    MessageHolder holder = DataTools.parse(data);
                    if (holder != null && mListener != null) {
                        mListener.onMessageReceiver(holder);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void destroy() {
        try {
            mIsRunning = false;
            if (mBufferReader != null) {
                mBufferReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
