package com.example.dl579.singltonesocket;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Client client = new Client();
    AudioManager am;
    Button StopButton;
    Intent intent;
    LocationManager locationManager;
    double latitude = 0.0;double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        StopButton = (Button) findViewById(R.id.StopButton);
        chkGpsService();
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        client.start();
        StopButton.setOnClickListener(this);
        startLocationService();
        checkData();
    }

//1000은 1초마다, 1은 1미터마다 해당 값을 갱신한다는 뜻으로, 딜레이마다 호출하기도 하지만
//위치값을 판별하여 일정 미터단위 움직임이 발생 했을 때에도 리스너를 호출 할 수 있다.


    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }


    public void checkData() {
        new Thread() {
            public void run() {
                while (true) {
                    if (client.message.equals("200")) {
                        SendSMS("01072267934", "이상훈");
                        client.message = "";
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_PLAY_SOUND);
                        intent = new Intent(MainActivity.this, MyService.class);
                        intent.putExtra("PlayOrStop", "200");
                        startService(intent);
                    }
                }
            }
        }.start();
    }

    public void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, msg, null, null);
    }


    private void startLocationService() {

        // get manager instance
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // set listener
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 0;


        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    }

    @Override
    public void onClick(View v) {
        client.SendData("asdasdqwezxc");
    }

    class GPSListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
             latitude = location.getLatitude();
             longitude = location.getLongitude();
            try {
                final JSONObject object = new JSONObject();
                object.put("위도", latitude);
                object.put("경도",longitude);
                TimerTask adTast = new TimerTask() {
                    public void run() {
                        client.SendData(object.toString());
                        Log.e("adTast ", "timer");
                    }
                };
                Timer timer = new Timer();
                timer.schedule(adTast, 0, 3000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            String msg = "Latitude : "+ latitude + "Longitude:"+ longitude;
            Log.e("GPSLocationService", msg);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}


