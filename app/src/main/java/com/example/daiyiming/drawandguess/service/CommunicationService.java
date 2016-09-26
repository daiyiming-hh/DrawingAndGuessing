package com.example.daiyiming.drawandguess.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.example.daiyiming.drawandguess.asynctask.ConnectAsyncTask;
import com.example.daiyiming.drawandguess.asynctask.ReaderAsyncTask;
import com.example.daiyiming.drawandguess.asynctask.WriterAsyncTask;
import com.example.daiyiming.drawandguess.constants.Config;
import com.example.daiyiming.drawandguess.enums.Type;
import com.example.daiyiming.drawandguess.interfaces.IWriterDataSetter;
import com.example.daiyiming.drawandguess.interfaces.OnConnectListener;
import com.example.daiyiming.drawandguess.interfaces.OnMessageReceiveListener;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by daiyiming on 2016/9/13.
 */

public class CommunicationService extends Service {

    private ServerSocket mServer = null;
    private Socket mSocket = null;
    private ConnectAsyncTask mConnectAsyncTask = null;
    private ReaderAsyncTask mReaderAsyncTask = null;
    private WriterAsyncTask mWriterAsyncTask = null;

    public class LocalBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }

    public interface OnPrepareListener {
        void onPrepareSuccess();

        void onPrepareError();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    /**
     * 准备连接事宜
     *
     * @param type     类型
     * @param listener 监听器
     */
    public void prepareConnect(Type type, final OnPrepareListener listener) {
        try {
            mConnectAsyncTask = new ConnectAsyncTask(new OnConnectListener() {
                @Override
                public void onConnected(Socket socket) {
                    if (socket != null) {
                        mSocket = socket;
                        if (listener != null) {
                            listener.onPrepareSuccess();
                        }
                    } else {
                        destroy();
                        if (listener != null) {
                            listener.onPrepareError();
                        }
                    }
                }
            });
            if (type == Type.SERVER) {
                mConnectAsyncTask.execute(mServer = new ServerSocket(Config.PORT));
            } else if (type == Type.CLIENT) {
                mConnectAsyncTask.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            destroy();
            if (listener != null) {
                listener.onPrepareError();
            }
        }
    }

    public void prepareCommunication(OnMessageReceiveListener messageReceiveListener) {
        try {
            mReaderAsyncTask = new ReaderAsyncTask(mSocket.getInputStream(), messageReceiveListener);
            mReaderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            mWriterAsyncTask = new WriterAsyncTask(mSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            destroy();
        }
    }

    public void destroy() {
        stopConnectAsyncTask();
        stopSocket();
        stopReaderAsyncTask();
        stopWriterAsyncTask();
        stopServer();
    }

    public IWriterDataSetter getWriterDataSetter() {
        return mWriterAsyncTask;
    }

    public void stopWriterAsyncTask() {
        try {
            if (mWriterAsyncTask != null) {
                mWriterAsyncTask.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopReaderAsyncTask() {
        try {
            if (mReaderAsyncTask != null && !mReaderAsyncTask.isCancelled()) {
                mReaderAsyncTask.destroy();
                mReaderAsyncTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        try {
            if (mServer != null && !mServer.isClosed()) {
                mServer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopSocket() {
        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.shutdownOutput();
                mSocket.shutdownInput();
                mSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopConnectAsyncTask() {
        try {
            if (mConnectAsyncTask != null && !mConnectAsyncTask.isCancelled()) {
                mConnectAsyncTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
