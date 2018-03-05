package com.example.dl579.waterproof_app;

import android.Manifest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity  {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    private static final int MY_PERMISSIONS_REQUEST_FineLocation_CONTACTS = 200;
    static Socket socket;
    private MyService2 mService;


    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyService2.MainServiceBinder binder = (MyService2.MainServiceBinder) service;
            mService = binder.getService(); //서비스 받아옴
            mService.registerCallback(mCallback); //콜백 등록
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    //서비스에서 아래의 콜백 함수를 호출하며, 콜백 함수에서는 액티비티에서 처리할 내용 입력
    private MyService2.ICallback mCallback = new MyService2.ICallback() {
        @Override
        public void recvData(double recvLongitude,double recvLatitude) {
            //처리할 일들..
            Double longitude = recvLongitude;
            Double latitude = recvLatitude;

            final JSONObject GPSInfo = new JSONObject();
            try {
                try {
                    GPSInfo.put("latitude", longitude.toString());
                    GPSInfo.put("longitude", latitude.toString());
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                           socket.emit("GPS",GPSInfo);
                        }
                    };
                    /////////// / Timer 생성 //////////////
                    Timer timer = new Timer();
                    timer.schedule(tt, 0, 3000);
                    //////////////////////////////////////
                Log.e("Gps 정보",GPSInfo.toString());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    private  void send_Message(){
        SendSMS("010-0000-0000", "현재 침수가 시작되고 있습니다. xxx님이 위험에 빠졌습니다.");
        Intent GpsService = new Intent(MainActivity.this, MyService2.class);
        Intent MusicService = new Intent(MainActivity.this, MyService.class);
        MusicService.putExtra(MyService.MESSAGE_KEY, true);
        bindService(GpsService,mConnection,BIND_AUTO_CREATE);
        startService(MusicService);
        startService(GpsService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
            try {
                socket = IO.socket("http://192.168.43.153:8801");
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.e("State===>", "Connect");
                    }
                }).on("androidWarn", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject jsonObject =(JSONObject)args[0];
                        try {
                            Log.e("androidWarn",jsonObject.getString("data"));
                            String level  = jsonObject.getString("data");
                            if(level.equals("1")){
                                send_Message();
                            }else if(level.equals("2")){
                                send_Message();
                            }else if(level.equals("3")){
                                send_Message();
                            }else if(level.equals("4")){
                                send_Message();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, msg, null, null);
    }
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }

    }


}

