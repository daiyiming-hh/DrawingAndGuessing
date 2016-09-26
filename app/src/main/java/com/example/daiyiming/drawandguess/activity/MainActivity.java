package com.example.daiyiming.drawandguess.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.daiyiming.drawandguess.R;
import com.example.daiyiming.drawandguess.enums.Type;
import com.example.daiyiming.drawandguess.fragment.ConnectMeFragment;
import com.example.daiyiming.drawandguess.fragment.ConnectTaFragment;
import com.example.daiyiming.drawandguess.fragment.GameFragment;
import com.example.daiyiming.drawandguess.fragment.SelectionFragment;
import com.example.daiyiming.drawandguess.interfaces.IWriterDataSetter;
import com.example.daiyiming.drawandguess.service.CommunicationService;

public class MainActivity extends AppCompatActivity {

    private CommunicationService mService = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((CommunicationService.LocalBinder) iBinder).getService();
            // 填充界面为选择界面
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_layout, new SelectionFragment(), SelectionFragment.class.getName());
            transaction.commitAllowingStateLoss();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService.destroy();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定服务
        Intent intent = new Intent(MainActivity.this, CommunicationService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void addConnectMeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_layout, new ConnectMeFragment(), ConnectMeFragment.class.getName());
        transaction.commitAllowingStateLoss();
    }

    public void addConnectTaFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_layout, new ConnectTaFragment(), ConnectTaFragment.class.getName());
        transaction.commitAllowingStateLoss();
    }

    public GameFragment addGameFragment() {
        GameFragment fragment = new GameFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_layout, fragment, ConnectTaFragment.class.getName());
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    public void startCommunication(Type type) {
        mService.prepareConnect(type, new CommunicationService.OnPrepareListener() {
            @Override
            public void onPrepareSuccess() {
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                GameFragment fragment = addGameFragment();
                mService.prepareCommunication(fragment);
            }

            @Override
            public void onPrepareError() {
                Toast.makeText(MainActivity.this, "连接准备失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    public IWriterDataSetter getWriterDataSetter() {
        return mService.getWriterDataSetter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.destroy();
        unbindService(mServiceConnection);
    }

}
