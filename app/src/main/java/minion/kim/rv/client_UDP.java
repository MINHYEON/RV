package minion.kim.rv;

/**
 * Created by minion on 15. 7. 24..
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;

import android.util.Log;


public class client_UDP implements Runnable{

    final ByteArrayOutputStream arrStream = new ByteArrayOutputStream();
    final DataOutputStream outStream = new DataOutputStream(arrStream);

    float[] value;
    byte[] buf;

    float yaw, pitch, roll;

    client_UDP(float _yaw, float _pitch, float _roll)
    {
        yaw = _yaw;
        pitch = _pitch;
        roll = _roll;
    }

    public void makeStream() throws Exception{
        outStream.writeFloat(yaw);
        outStream.writeFloat(pitch);
        outStream.writeFloat(roll);

        outStream.close();

        buf = arrStream.toByteArray();
    }

    public void run(){

        try {

            makeStream();

            InetAddress serverAddr = InetAddress.getByName("192.168.1.51");

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket pkt = new DatagramPacket(buf, buf.length, serverAddr, 9999);

            socket.send(pkt);
            socket.close();
        } catch (Exception e) {
            Log.d("UDP","err");
        }
    }

}
