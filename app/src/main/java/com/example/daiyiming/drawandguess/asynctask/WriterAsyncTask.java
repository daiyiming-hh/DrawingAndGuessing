package com.example.daiyiming.drawandguess.asynctask;

import com.example.daiyiming.drawandguess.interfaces.IWriterDataSetter;
import com.example.daiyiming.drawandguess.view.Point;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daiyiming on 2016/9/19.
 */

public class WriterAsyncTask implements IWriterDataSetter {

    private BufferedWriter mBufferWriter = null;
    private StringBuffer mDataBuffer = null;
    private ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

    public WriterAsyncTask(OutputStream outputStream) {
        mBufferWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        mDataBuffer = new StringBuffer();
    }

    private void sendData(final String data) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mDataBuffer.append(data);
                    if (mDataBuffer.length() > 0) {
                        mDataBuffer.append("\n");
                        String data = mDataBuffer.toString();
                        mDataBuffer.delete(0, mDataBuffer.length());
                        mDataBuffer.setLength(0);
                        mBufferWriter.write(data, 0, data.length());
                        mBufferWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void destroy() {
        try {
            if (mBufferWriter != null) {
                mBufferWriter.close();
            }
            if (mSingleThreadExecutor != null && !mSingleThreadExecutor.isShutdown()) {
                mSingleThreadExecutor.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDrawData(List<Point> points) {
        sendData(DataTools.compileDrawData(points));
    }

    @Override
    public void setMessageData(String message) {
        sendData(DataTools.compileMessageData(message));
    }

    @Override
    public void setClearData() {
        sendData(DataTools.compileClearData());
    }

}
