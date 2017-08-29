package com.example.dl579.singltonesocket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
    MediaPlayer mp3;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = intent.getExtras().getString("PlayOrStop");
        if(message.equals("200")) {
            mp3 = MediaPlayer.create(this, R.raw.music);
            mp3.start();
            Intent mMainIntent = new Intent(this, MainActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(
                    this, 1, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.btn_star)
                            .setContentTitle("위급상황 위급상황")
                            .setContentIntent(mPendingIntent)
                            .setContentText("현재 집이 침수 되고있습니다 대피하세요");
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(001, mBuilder.build());
        }
        else {
            mp3.stop();
            mp3.release();
        }
        return START_NOT_STICKY;
    }
}
