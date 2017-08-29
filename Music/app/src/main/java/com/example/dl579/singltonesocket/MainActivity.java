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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Client client = new Client();
    Button PlayButton;
    Button StopButton;
    Button SendMsessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlayButton = (Button)findViewById(R.id.PlayButton);
        StopButton = (Button)findViewById(R.id.StopButton);
        SendMsessage = (Button)findViewById(R.id.SendMessage);
        PlayButton.setOnClickListener(this);
        StopButton.setOnClickListener(this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        SendMsessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSMS("01072267934","현영이형");
            }
        });
        client.start();
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        Log.e("","클릭");
        if(v.getId() == R.id.PlayButton){
            intent.putExtra("PlayOrStop", "200");
        }
        else{
            intent.putExtra("PlayOrStop", "400");
        }
        startService(intent);
    }

    public void SendSMS(String number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number,null,msg,null,null);
    }
}
