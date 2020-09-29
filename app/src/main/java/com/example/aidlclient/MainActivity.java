package com.example.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aidlservice.IaidlSun;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private IaidlSun aidlTest;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addBtn = (Button)findViewById(R.id.btn_add);
        Button bindBtn = (Button)findViewById(R.id.btn_bind);
        addBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
    }
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlTest = IaidlSun.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlTest = null;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                try{
                   int a =  aidlTest.addTest(3,5);
                    Toast.makeText(this,"结果是："+a,Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.btn_bind:
                btnBindService();
                break;
        }
    }

    public void btnBindService(){
        //Intent intent = new Intent("com.example.aidlservice.IaidlSun");
        Intent intent = new Intent();
        intent.setPackage("com.example.aidlservice");
        intent.setAction("android.intent.action.serviceact");
        boolean isbind =  bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
        Toast.makeText(this,"bind is "+isbind,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aidlTest != null) {
            unbindService(serviceConnection);
        }
    }
}
