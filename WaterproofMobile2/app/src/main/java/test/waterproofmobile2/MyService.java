package test.waterproofmobile2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    MediaPlayer mp3;
    int remainTime = 0;
    long currentState = 0;
    long destState = 0;

    public MyService() {
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
        if (message.equals(Resource.MESSAGE_CALL)) {
            if (level.equals(Resource.CALL_WARN)) {
                mp3 = MediaPlayer.create(this, R.raw.level3);
            } else if (level.equals(Resource.CALL_EMER)) {
                mp3 = MediaPlayer.create(this, R.raw.level4);
            }
            //mp3.setLooping(true);
            if (currentState >= destState) {
                currentState = System.currentTimeMillis();
                destState = System.currentTimeMillis() + 15 * 1000;
                mp3.start();

            } else {
                currentState = System.currentTimeMillis();
            }
        }
        else{
            Log.e("elsecase","");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
