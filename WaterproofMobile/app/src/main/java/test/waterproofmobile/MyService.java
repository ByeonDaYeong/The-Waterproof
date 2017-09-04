package test.waterproofmobile;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jiharu on 2017. 9. 1..
 */

public class MyService extends Service{
    MediaPlayer mp3;
    public MyService(){
        Log.e("MyService: ", "Make");
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = intent.getExtras().getString("message");
        String level = intent.getExtras().getString("level");
        Log.e("Entry","MyService");
        if(message.equals(Resource.MESSAGE_CALL)){
            if(level.equals(Resource.CALL_WARN)){
                mp3 = MediaPlayer.create(this, R.raw.level3);
            }else if(level.equals(Resource.CALL_EMER)){
                mp3 = MediaPlayer.create(this, R.raw.level3);
            }
            mp3.setLooping(true);
            mp3.start();
            Intent mMainIntent = new Intent(this, MainActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(
                    this, 1, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
