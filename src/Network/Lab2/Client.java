package Network.Lab2;
import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            int port = 12345;
            InetAddress address = InetAddress.getByName("localhost");
            String directoryPath = "C:\\Users\\nilay\\Desktop\\SE375";

            byte[] buffer = null;
            DatagramPacket packet = null;
            String response = null;

            socket = new DatagramSocket();

            File directory = new File(directoryPath);
            File[] files = directory.listFiles();

            for(File file: files) {
                String message = file.getName();
                buffer = message.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            }

            // Prompt the user for a district name or "EXIT" to terminate the application
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Send a district name to server to see stats (type 'EXIT' to quit): ");
                String input = reader.readLine();

                // Send the district name to the server and wait for a response
                buffer = input.getBytes();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);

                // Display the statistics received from the server
                buffer = new byte[1024];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                response = new String(packet.getData(), 0, packet.getLength());
                try{
                    response.split(",")[1].trim();
                    System.out.printf("Here are the stats for district %s\n", input);
                    System.out.printf("Total number of passengers: %s\n", response.split(",")[1].trim());
                    System.out.printf("Total number of trips: %s\n", response.split(",")[0].trim());
                    int p = Integer.parseInt(response.split(",")[1].trim());
                    int t = Integer.parseInt(response.split(",")[0].trim());
                    System.out.printf("Average number of passengers per trip: %.2f\n", (double) p / t);
                } catch (Exception e){
                    if(!response.equals("Exiting...")){
                        System.out.println(response);
                    }
                }

                if (input.equals("EXIT")) {
                    System.out.println("Exiting...");
                    break;
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
