package com.example.daiyiming.drawandguess.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daiyiming.drawandguess.R;
import com.example.daiyiming.drawandguess.activity.MainActivity;
import com.example.daiyiming.drawandguess.asynctask.MessageHolder;
import com.example.daiyiming.drawandguess.constants.Command;
import com.example.daiyiming.drawandguess.interfaces.IWriterDataSetter;
import com.example.daiyiming.drawandguess.interfaces.OnMessageReceiveListener;
import com.example.daiyiming.drawandguess.view.DrawCanvas;
import com.example.daiyiming.drawandguess.view.Point;
import com.example.daiyiming.drawandguess.view.SocketCanvasHandler;
import com.example.daiyiming.drawandguess.view.TouchCanvasHandler;

import java.util.List;

public class GameFragment extends Fragment implements OnMessageReceiveListener, View.OnTouchListener, View.OnClickListener {

    private DrawCanvas mDcGameView = null;
    private SocketCanvasHandler mSocketHandler = null;
    private TouchCanvasHandler mTouchHandler = null;
    private MainActivity mMainActivity = null;
    private IWriterDataSetter mDataSetter = null;
    private EditText mEdtMessage = null;
    private Vibrator mVibrator = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        view.setOnTouchListener(this);
        mDcGameView = (DrawCanvas) view.findViewById(R.id.dc_game_view);
        mDcGameView.setKeepScreenOn(true);
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        view.findViewById(R.id.btn_clear).setOnClickListener(this);
        mEdtMessage = (EditText) view.findViewById(R.id.edt_message);
        mSocketHandler = new SocketCanvasHandler(mDcGameView);
        mTouchHandler = new TouchCanvasHandler(mDcGameView, mDcGameView, mDataSetter = mMainActivity.getWriterDataSetter());
        mVibrator = (Vibrator) mMainActivity.getSystemService(Context.VIBRATOR_SERVICE);
        return view;
    }

    @Override
    public void onMessageReceiver(MessageHolder holder) {
        long[] pattern = {300};
        mVibrator.vibrate(pattern, -1);
        switch (holder.command) {
            case Command.DRAW: {
                drawPoints(holder.points);
            }
            break;
            case Command.CLEAR: {
                mDcGameView.clearCanvas();
            }
            break;
            case Command.MESSAGE: {
                showMessage(holder.message);
            }
            break;
        }
    }

    private void showMessage(final String message) {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(mMainActivity, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private void drawPoints(List<Point> points) {
        for (int i = 1; i < points.size(); i ++) {
            Point startPoint = points.get(i - 1);
            Point endPoint = points.get(i);
            if (!startPoint.isBlankPoint() && !endPoint.isBlankPoint()) {
                mSocketHandler.addLine(points.get(i - 1), points.get(i));
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send: {
                String message = mEdtMessage.getText().toString();
                if (!TextUtils.isEmpty(message) && mDataSetter != null) {
                    mDataSetter.setMessageData(message);
                    mEdtMessage.setText("");
                }
            }
            break;
            case R.id.btn_clear: {
                if (mDataSetter != null) {
                    mDataSetter.setClearData();
                    mDcGameView.clearCanvas();
                }
            }
            break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mVibrator.cancel();
    }
}
