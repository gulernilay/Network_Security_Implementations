package Network.SocketProgramming.UDP;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String args[]) {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            DatagramSocket  socket = new DatagramSocket();
            String message1 = "Hello! This is client calling...";
            byte data[] = message1.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, 8888);
            socket.send(packet); // send the packet



            Date sendTime = new Date(); // note the time of sending the message
            socket.receive(packet); // receive the packet
            String message2 = new String(packet.getData());
            Date receiveTime = new Date(); // note the time of receiving the message
            System.out.println((receiveTime.getTime() - sendTime.getTime()) + " milliseconds echo time for " + message2);
        } catch (UnknownHostException e) {
        } catch (SocketException e) {
        } catch (IOException e) {}
    }
}
