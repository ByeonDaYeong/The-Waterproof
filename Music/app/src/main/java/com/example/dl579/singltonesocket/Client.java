package com.example.dl579.singltonesocket;


import android.util.Log;


import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by dl579 on 2017-08-29.
 */

public class Client extends Thread {
    Socket socket;

    String message = "";
    public Client() {
        try {
            socket = IO.socket("http://155.230.221.13:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Log.e("connect","connect");
                }
            }).on("androidWarn", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try
                    {
                    JSONObject obj = (JSONObject)args[0];
                   message = obj.get("data").toString();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).on("addUser", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void SendLocationMessage(JSONObject message)
    {
        socket.emit("Location",message);
    }


/*
InputStream dataInputStream = null;
    OutputStream outputStream;
    public void run() {
        try {
            socket = new Socket("192.168.43.153", 7777);
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

                } catch (IOException e)
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
    }*/

}
