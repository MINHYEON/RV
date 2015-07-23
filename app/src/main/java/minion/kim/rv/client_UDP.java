package minion.kim.rv;

/**
 * Created by minion on 15. 7. 24..
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class client_UDP {

    byte[] buf;

    client_UDP(byte[] value){
        buf = value;
    }

    public void run(){

        try {
            InetAddress serverAddr = InetAddress.getByName("192.168.0.11");

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket pkt = new DatagramPacket(buf, buf.length, serverAddr, 9999);

            socket.send(pkt);
            socket.close();
        } catch (Exception e) {

        }
    }

}
