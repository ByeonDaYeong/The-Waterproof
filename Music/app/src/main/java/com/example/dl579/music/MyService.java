package com.example.dl579.music;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

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
                        .setContentTitle("음악 플레이테스트")
                        .setContentIntent(mPendingIntent)
                        .setContentText("백그라운드에서 음악이 플레이되고있어요");
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
