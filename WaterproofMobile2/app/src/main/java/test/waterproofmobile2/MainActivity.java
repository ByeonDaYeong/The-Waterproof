package test.waterproofmobile2;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    Intent intent;
    AudioManager am;
    LocationManager locationManager;

    /*Instance*/
    double latitude = 0.0;
    double longitude = 0.0;
    boolean submitA = false;
    boolean submitB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /*GPS권한 부여및 sms권한 부여*/
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        chkGpsService();
        startLocationService();

        try {
            socket = IO.socket("http://155.230.221.187:8801");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.e("State===>", "Connect");
                }

            }).on("androidWarn", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject) args[0];
                    try {
                        String message = obj.get("data").toString();
                        Log.e("message",message );
                        intent = new Intent(MainActivity.this, MyService.class);
                        if (message.equals("0")) {
                            Log.e("call: ", 0+"");
                            Log.e("이상훈", submitA+":"+submitB);
                            intent.putExtra("message",Resource.MESSAGE_CALL);
                            intent.putExtra("level",Resource.CALL_WARN);
                            if(submitA==false){
                                SendSMS("01072267934", "이상훈입니다.");
                                submitA=true;
                            }
                            else if(submitB==false){
                                SendSMS("01041724516", "감태정입니다.");
                                submitB=true;
                            }
                            startService(intent);
                        } else if (message.equals("1")) {
                            Log.e("call: ", 1+"");
                            intent.putExtra("message",Resource.MESSAGE_CALL);
                            intent.putExtra("level",Resource.CALL_EMER);
                            if(submitA==false){
                                Log.e("이상훈", "이상훈");
                                SendSMS("01072267934", "이상훈입니다.");
                                submitA=true;
                            }
                            else if(submitB==false){
                                SendSMS("01041724516", "감태정입니다.");
                                submitB=true;
                            }
                            startService(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void startLocationService() {

        // get manager instance
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // set listener
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 0;


        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            try {
                final JSONObject object = new JSONObject();
                object.put("latitude", latitude);
                object.put("longtitude", longitude);
                TimerTask adTast = new TimerTask() {
                    public void run() {
                        Log.e("Location", "위도" + latitude);
                        socket.emit("GPS", object);
                        Log.e("adTast ", "timer");
                    }
                };
                Timer timer = new Timer();
                timer.schedule(adTast, 0, 3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, msg, null, null);
    }
}
