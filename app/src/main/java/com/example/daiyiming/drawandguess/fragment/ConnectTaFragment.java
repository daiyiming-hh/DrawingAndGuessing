package com.example.daiyiming.drawandguess.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daiyiming.drawandguess.R;
import com.example.daiyiming.drawandguess.activity.MainActivity;
import com.example.daiyiming.drawandguess.constants.Config;
import com.example.daiyiming.drawandguess.enums.Type;

public class ConnectTaFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private MainActivity mMainActivity = null;
    private EditText mEdiIP = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_ta, container, false);
        view.setOnTouchListener(this);
        mEdiIP = (EditText) view.findViewById(R.id.edt_ip);
        view.findViewById(R.id.btn_connect).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        String ip = mEdiIP.getText().toString();
        if (!TextUtils.isEmpty(ip)) {
            view.setEnabled(false);
            Config.HOST = ip;
            mMainActivity.startCommunication(Type.CLIENT);
        } else {
            Toast.makeText(getActivity(), "无效的ip地址", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
