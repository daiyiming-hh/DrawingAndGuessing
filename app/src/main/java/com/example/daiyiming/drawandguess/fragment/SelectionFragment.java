package com.example.daiyiming.drawandguess.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daiyiming.drawandguess.R;
import com.example.daiyiming.drawandguess.activity.MainActivity;

public class SelectionFragment extends Fragment implements View.OnClickListener {

    private MainActivity mMainActivity = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        view.findViewById(R.id.tv_connect_me).setOnClickListener(this);
        view.findViewById(R.id.tv_connect_ta).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_connect_me: {
                mMainActivity.addConnectMeFragment();
            } break;
            case R.id.tv_connect_ta: {
                mMainActivity.addConnectTaFragment();

            } break;
        }
    }
}
