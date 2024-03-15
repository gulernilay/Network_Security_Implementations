package Security.LAB2;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client {
    private static final String address = "localhost";
    private static final int port = 5000;
    private static final String SECRET_KEY_FILE = "secret.key";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Connected to the server.");
            sendFiles(socket);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String query;
            while (true) {
                System.out.print("Enter a query (or 'EXIT' to quit): ");
                query = input.readLine();
                if (query.equals("EXIT")) {
                    sendQuery(socket, query);
                    break;
                }
                sendQuery(socket, query);
                String response = receiveMessage(socket);
                if (authenticateMessage(response)) {
                    String[] statistics = response.split(",");
                    int totalPassenger = Integer.parseInt(statistics[0]);
                    int totalTrips = Integer.parseInt(statistics[1]);
                    double averagePassenger = Double.parseDouble(statistics[2]);
                    System.out.println("Total Passenger: " + totalPassenger);
                    System.out.println("Total Trips: " + totalTrips);
                    System.out.println("Average Number of Passengers: " + averagePassenger);
                } else {
                    System.out.println("Error: Message authentication failed.");
                }
            }
            socket.close();
            input.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFiles(Socket socket) throws IOException {
        File[] files = new File("data").listFiles();
        if (files != null) {
            sendFileCount(socket, files.length);
            for (File file : files) {
                sendFile(socket, file);
            }
        }
    }

    private static void sendFile(Socket socket, File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sendMessage(socket, line);
            }
        }
    }

    private static void sendFileCount(Socket socket, int value) throws IOException {
        OutputStream output = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(output);
        dataOutputStream.writeInt(value);
        dataOutputStream.flush();
    }

    private static void sendMessage(Socket socket, String message) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.println(message);
        writer.flush();
    }

    private static void sendQuery(Socket socket, String query) throws IOException {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            byte[] mac = computeMAC(query);
            if (mac != null) {
                String messageWithMAC = bytesToHex(mac) + query;
                writer.println(messageWithMAC);
                writer.flush();
            } else {
                System.out.println("Error: Message authentication failed.");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private static String receiveMessage(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        return reader.readLine();
    }

    private static boolean authenticateMessage(String message) {
        try {
            String receivedMACString = message.substring(0, 40);
            String receivedMessage = message.substring(40);
            byte[] receivedMAC = hexToBytes(receivedMACString);

            byte[] computedMAC = computeMAC(receivedMessage);

            return MessageDigest.isEqual(receivedMAC, computedMAC);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] computeMAC(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] key = readSecretKey();
        digest.update(key);
        return digest.digest(message.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] readSecretKey() {
        try {
            FileInputStream fileInputStream = new FileInputStream(SECRET_KEY_FILE);
            byte[] key = new byte[16];
            int bytesRead = fileInputStream.read(key);
            if (bytesRead != 16) {
                System.out.println("Error reading secret key.");
                System.exit(1);
            }
            fileInputStream.close();
            return key;
        } catch (IOException e) {
            System.out.println("Error reading secret key file.");
            System.exit(1);
        }
        return null;
    }

    private static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}



























































