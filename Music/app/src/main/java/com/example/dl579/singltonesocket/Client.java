package com.example.dl579.singltonesocket;


import android.app.Activity;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by dl579 on 2017-08-29.
 */

public class Client extends Thread {
    Socket socket;
    InputStream dataInputStream = null;
    String message="";

    public void run() {
        try {
            socket = new Socket("192.168.43.153", 7777);
            while (true) {
                RecieveData(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RecieveData(final Socket socket) {
        try {
            dataInputStream = socket.getInputStream();
            byte arr[] = new byte[4096];
            int len = dataInputStream.read(arr);
            byte arr2[] = new byte[len];
            System.arraycopy(arr, 0, arr2, 0, len);
            message = new String(arr2, "UTF-8");
            Log.e("recv data: ", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
