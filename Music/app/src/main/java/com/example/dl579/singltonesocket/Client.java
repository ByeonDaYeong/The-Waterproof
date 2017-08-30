package com.example.dl579.singltonesocket;



import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by dl579 on 2017-08-29.
 */

public class Client extends Thread {
    Socket socket;
    InputStream dataInputStream = null;
    OutputStream outputStream;
    String message="";

    public void run() {
        try {
            socket = new Socket("192.168.0.3", 7777);
            outputStream = socket.getOutputStream();
            while (true) {
                RecieveData(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendData(final String message)
    {
        new Thread() {
            public void run() {
                try
                {
                    Log.e("send", message);
                    outputStream.write(message.getBytes());

                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        }.start();
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
