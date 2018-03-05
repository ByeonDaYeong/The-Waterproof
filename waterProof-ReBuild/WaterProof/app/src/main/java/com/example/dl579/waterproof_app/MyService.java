package com.example.dl579.waterproof_app;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class MyService extends Service {
    static String MESSAGE_KEY;
    MediaPlayer mp;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.level3);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        boolean Message = intent.getExtras().getBoolean(MyService.MESSAGE_KEY);
        if(Message)
        {
                        mp.start();
        }else{
                        mp.stop();
                        mp.release();
        }
        return START_NOT_STICKY;
    }
}
