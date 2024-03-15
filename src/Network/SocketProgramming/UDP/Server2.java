package Network.SocketProgramming.UDP;

import java.net.*;
import java.io.*;
import java.util.*;


public class Server2 {
    public static void main(String args[]) throws Exception {
        // STEP 1: Socket yarat
        DatagramSocket socket = new DatagramSocket(8888);
        // Gelen -Giden byte veriler toplanır
        byte data[] = new byte[150];
        // Datagram Packet = data + data length
        DatagramPacket packet = new DatagramPacket(data, data.length);

        while (true) {
            socket.receive(packet);

            byte packetData[] = packet.getData();
            int packetSize = packet.getLength();
            // gelen paketin  byte ve size ı birleştirilierek string oluşturulur.
            String s2 = new String(packetData, 0, packetSize);

            System.out.println(new Date() + "  " + packet.getAddress() + " : " + packet.getPort() + " " + s2);
            socket.send(packet);
        }
    }
}