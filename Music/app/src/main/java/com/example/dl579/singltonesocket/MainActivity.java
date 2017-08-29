package com.example.dl579.singltonesocket;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Client client = new Client();
    String Message;
    Button PlayButton;
    Button StopButton;
    Button SendMsessage;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlayButton = (Button) findViewById(R.id.PlayButton);
        StopButton = (Button) findViewById(R.id.StopButton);
        SendMsessage = (Button) findViewById(R.id.SendMessage);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        client.start();
        checkData();
    }

    public void checkData() {
        new Thread() {
            public void run() {
                while (true) {
                    if (client.message.equals("200")) {
                        SendSMS("01072267934", "이상훈");
                        client.message = "";
                        intent = new Intent(MainActivity.this,MyService.class);
                        intent.putExtra("PlayOrStop","200");
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
}
