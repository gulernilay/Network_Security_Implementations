package Network.SocketProgramming.UDP;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);
        byte[] data= new byte[150];
        DatagramPacket packet= new DatagramPacket(data,data.length);
        while(true){

            socket.receive(packet);
            byte[] data2= packet.getData();
            int size=packet.getLength();
            System.out.println(new Date() + "  " + packet.getAddress() + " : " + packet.getPort() + " " );
            socket.send(packet);


        }

    }
}
