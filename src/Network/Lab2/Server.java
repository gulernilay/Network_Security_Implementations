package Network.Lab2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        Map<String, int[]> data = new HashMap<>();
        Lock lock = new ReentrantLock();

        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(PORT);
            for(int i=0; i<8; i++) {
                System.out.println("Waiting for a packet...");
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String filename = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received packet from: " + packet.getAddress() + ":" + packet.getPort() + ", Message: " + filename + ". Assigning thread...");

                new Thread(() -> {
                    Map<String, int[]> result = null;
                    try {
                        result = processCsvFile(new File("C:\\Users\\nilay\\Desktop\\SE375" + filename));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    lock.lock();
                    try {
                        data.putAll(result);
                    } finally {
                        lock.unlock();
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }

        System.out.println("Processing done! Waiting for a query...");

        try{
            while(true) {
                System.out.println();
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String filename = new String(packet.getData(), 0, packet.getLength()).replace(".csv", "");

                if (filename.equals("EXIT")) {
                    System.out.println("Received EXIT. Exiting...");

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    byte[] response = "Exiting...".getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(response, response.length, address, port);
                    socket.send(responsePacket);
                    break;
                }

                System.out.println("Received query: " + filename);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                int[] stats = null;
                byte[] response = null;
                if(data.containsKey(filename)){
                    stats = data.get(filename);
                    response = Arrays.toString(stats).replace("[", "").replace("]", "").getBytes();
                }else{
                    response = "District doesn't exist".getBytes();
                }
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, address, port);
                socket.send(responsePacket);
                System.out.println("Reply sent to client. Awaiting for query...");
            }
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    public static Map<String, int[]> processCsvFile(File file) throws IOException {
        Map<String, int[]> stats = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String district = parts[0].trim();
            int trips = Integer.parseInt(parts[2].trim());
            int passengers = Integer.parseInt(parts[3].trim());
            int[] values = stats.getOrDefault(file.getName().replace(".csv", ""), new int[2]);
            values[0] += trips;
            values[1] += passengers;
            stats.put(file.getName().replace(".csv", ""), values);
        }
        reader.close();
        return stats;
    }
}