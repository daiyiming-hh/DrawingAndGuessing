package com.example.daiyiming.drawandguess.asynctask;

import android.os.AsyncTask;

import com.example.daiyiming.drawandguess.constants.Config;
import com.example.daiyiming.drawandguess.interfaces.OnConnectListener;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by daiyiming on 2016/9/13.
 */

public class ConnectAsyncTask extends AsyncTask<ServerSocket, Void, Socket> {

    private OnConnectListener mListener = null;

    public ConnectAsyncTask(OnConnectListener listener) {
        mListener = listener;
    }

    @Override
    protected Socket doInBackground(ServerSocket... serverSockets) {
        try {
            if (serverSockets.length > 0) { // 服务端
                return serverSockets[0].accept();
            } else { // 客户端
                return new Socket(Config.HOST, Config.PORT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        super.onPostExecute(socket);
        if (mListener != null) {
            mListener.onConnected(socket);
        }
    }
}
