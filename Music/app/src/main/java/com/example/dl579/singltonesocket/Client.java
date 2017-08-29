package com.example.dl579.singltonesocket;



import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by dl579 on 2017-08-29.
 */

public class Client extends Thread {
    Socket socket;

    public void run() {
        try {
            socket = new Socket("192.168.43.153", 7777);
            ObjectOutputStream outputStream =
                                                new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("Hello Android Town on Android");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}