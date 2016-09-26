package com.example.daiyiming.drawandguess.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daiyiming.drawandguess.R;
import com.example.daiyiming.drawandguess.activity.MainActivity;
import com.example.daiyiming.drawandguess.enums.Type;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ConnectMeFragment extends Fragment implements View.OnTouchListener {

    private MainActivity mMainActivity = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mMainActivity.startCommunication(Type.SERVER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_me, container, false);
        view.setOnTouchListener(this);
        TextView tvIP = (TextView) view.findViewById(R.id.tv_ip);
        String ip = getIP();
        if (!TextUtils.isEmpty(ip)) {
            tvIP.setText(ip);
        } else {
            tvIP.setText("获取IP地址失败");
        }
        return view;
    }

    private String getIP() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
