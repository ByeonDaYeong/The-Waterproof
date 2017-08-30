import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) {
        try {
            Socket socket = IO.socket("http://localhost:8801");
            socket.on(Socket.EVENT_CONNECT, (Object... data)->{
                socket.emit("foo", "hi");
                //socket.disconnect();

            }).on("event", (Object... data)->{


            }).on(Socket.EVENT_DISCONNECT, (Object... data)->{

            });
            socket.connect();
            socket.emit("foo", "drive");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
       //System.exit(1);
    }
}
