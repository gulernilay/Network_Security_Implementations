package Network.SocketProgramming.UDP;

import java.util.*;
import java.net.*;
import java.io.*;


public class Client2{
    public static void main(String[] args) throws Exception{

        DatagramPacket packet;
        DatagramSocket socket;
        InetAddress address;
        try{
            address = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            String message="Hello wORLD";
            packet= new DatagramPacket(message.getBytes(), message.length(),address,8888);
            socket.send(packet);
            Date sendTime = new Date(); // note the time of sending the message

            // Receive message:
            socket.receive(packet);
            String message2 = new String(packet.getData());
            Date receiveTime = new Date(); // note the time of receiving the message
            System.out.println((receiveTime.getTime() - sendTime.getTime()) + " milliseconds echo time for " + message2);
        } catch (UnknownHostException e) {
        } catch (SocketException e) {
        } catch (IOException e) {}
    }
}


