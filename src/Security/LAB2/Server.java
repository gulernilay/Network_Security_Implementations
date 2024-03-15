package Security.LAB2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server {
    private static final int port = 5000;

    public static void main(String[] args) {
        try {
            ServerSocket server_Socket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket client_Socket = server_Socket.accept();
                System.out.println("Client connected : " + client_Socket.getInetAddress().getHostAddress());
                handleClient(client_Socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticate_Message(String message) throws NoSuchAlgorithmException {
        String received_MAC = message.substring(0, 40);
        String query = message.substring(40);
        String computedMAC = ToHex(compute_MAC(query));
        return received_MAC.equals(computedMAC);
    }

    private static void handleClient(Socket socket) throws IOException {
        try (
                BufferedReader readerX = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writerX = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            String received_Message = readerX.readLine();

            if (authenticate_Message(received_Message)) {

                String received_MAC = received_Message.substring(0, 40);
                String QUERY = received_Message.substring(40);

                String RESULT = processQuery(QUERY);
                send_Message(writerX, received_MAC + RESULT);
            } else {
                send_Message(writerX, "Error: Message authentication failed");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    private static String processQuery(String queryX) throws IOException, NoSuchAlgorithmException {
        if (queryX.equals("TOTAL_PASSENGERS")) {
            int total_Passengers = countTotalPassengers();
            return "Total Passengers: " + total_Passengers;
        } else if (queryX.equals("TOTAL_TRIPS")) {
            int total_Trips = countTotalTrips();
            return "Total Trips: " + total_Trips;
        } else if (queryX.equals("AVERAGE_PASSENGERS")) {
            double average_Passengers = calculateAveragePassengers();
            return "Average Passengers: " + average_Passengers;
        } else {
            return "Invalid query";
        }
    }

    private static int countTotalPassengers() throws IOException {
        int totalPassengersX = 0;
        File[] fileX = new File("data").listFiles();
        if (fileX != null) {
            for (File file : fileX) {
                try (BufferedReader readerX = new BufferedReader(new FileReader(file))) {
                    String lineX;
                    while ((lineX = readerX.readLine()) != null) {
                        totalPassengersX++;
                    }
                }
            }
        }
        return totalPassengersX;
    }

    private static int countTotalTrips() {
        File[] filesX = new File("data").listFiles();
        if (filesX != null) {
            return filesX.length;
        } else {
            return 0;
        }
    }

    private static double calculateAveragePassengers() throws IOException {
        int total_PassengersX = countTotalPassengers();
        int total_TripsX = countTotalTrips();
        if (total_TripsX > 0) {
            return (double) total_PassengersX / total_TripsX;
        } else {
            return 0;
        }
    }

    private static void send_Message(BufferedWriter writer, String MESSAGE_X) throws IOException {
        byte[] MAC = new byte[0];
        try {
            MAC = compute_MAC(MESSAGE_X);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String messageWithMAC = ToHex(MAC) + MESSAGE_X;
        writer.write(messageWithMAC);
        writer.newLine();
        writer.flush();
    }

    private static byte[] compute_MAC(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        return digest.digest(message.getBytes(StandardCharsets.UTF_8));
    }

    private static String ToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}



























