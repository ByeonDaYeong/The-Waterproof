package test.waterproofmobile;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    Intent intent;
    AudioManager am;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        /*Open WebSocket Initialize*/
        try {
            socket = IO.socket("http://155.230.221.87:8801");
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
                            intent.putExtra("message",Resource.MESSAGE_CALL);
                            intent.putExtra("level",Resource.CALL_WARN);
                            startService(intent);
                        } else if (message.equals("1")) {
                            Log.e("call: ", 1+"");
                            intent.putExtra("message",Resource.MESSAGE_CALL);
                            intent.putExtra("message",Resource.CALL_EMER);
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
}
