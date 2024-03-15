package Network.Lab1;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Awaiting for connection");

            Socket socket = serverSocket.accept();
            System.out.println("Connection established with " + socket.getInetAddress().getHostAddress());

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            // Receive file names from client
            byte[] fileNameBytes = new byte[1024];
            int fileNameLength = is.read(fileNameBytes);
            String fileNames = new String(fileNameBytes, 0, fileNameLength);
            String[] files = fileNames.split(",");
            for (String file : files) {
                System.out.println("Received File : " + file);
            }
            System.out.println("File processing done. Awaiting for query...");

            // Process files and create HashMap
            HashMap<String, Result> resultMap = processFiles(files);

            // Convert HashMap to string and send to client
            String resultMapString = hashMapToString(resultMap);
            os.write(resultMapString.getBytes());

            // Handle queries from client
            handleQueries(socket, os, is, resultMap);

            // Close connections
            os.close();
            is.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Result> processFiles(String[] files) {
        HashMap<String, Result> resultMap = new HashMap<>();

        // Process each file
        for (String fileName : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue; // Skip header line
                    }
                    String[] data = line.split(",");
                    String district = data[0];
                    int trips = Integer.parseInt(data[2]);
                    int passengers = Integer.parseInt(data[3]);

                    Result result = resultMap.getOrDefault(district, new Result());
                    result.totalTrips += trips;
                    result.totalPassengers += passengers;
                    resultMap.put(district, result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculate average passenger per trip for each district
        for (Map.Entry<String, Result> entry : resultMap.entrySet()) {
            Result result = entry.getValue();
            result.avgPassengerPerTrip = (double) result.totalPassengers / result.totalTrips;
        }

        return resultMap;
    }

    private static String hashMapToString(HashMap<String, Result> resultMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Result> entry : resultMap.entrySet()) {
            String district = entry.getKey();
            Result result = entry.getValue();
            sb.append("District: ").append(district).append("\n");
            sb.append("Total Trips: ").append(result.totalTrips).append("\n");
            sb.append("Total Passengers: ").append(result.totalPassengers).append("\n");
            sb.append("Average Passenger per Trip: ").append(result.avgPassengerPerTrip).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void handleQueries(Socket socket, OutputStream os, InputStream is, HashMap<String, Result> resultMap) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(os, true);

            String query;
            while ((query = br.readLine()) != null) {
                System.out.println("beneneeeeee");
                System.out.println("Received query: " + query);

                if (query.equalsIgnoreCase("EXIT")) {
                    break;
                }

                Result result = resultMap.get(query);
                if (result != null) {
                    pw.println("District: " + query);
                    pw.println("Total Trips: " + result.totalTrips);
                    pw.println("Total Passengers: " + result.totalPassengers);
                    pw.println("Average Passenger per Trip: " + result.avgPassengerPerTrip);
                    pw.println();

                    System.out.println("Reply sent to client. Awaiting for query...");
                } else {
                    pw.println("Invalid district");

                    System.out.println("Invalid query. Reply sent to client. Awaiting for query...");
                }

                System.out.println("Awaiting for query...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    static class Result {
        int totalTrips;
        int totalPassengers;
        double avgPassengerPerTrip;
    }
}
