package Network.Lab1;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception{
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Client is connected to server");

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Send file names to server
            String fileNames = "Balcova.csv,Karsiyaka.csv,Bornova.csv,Gaziemir.csv,Karabaglar.csv,Konak.csv,Seferihisar.csv,Urla.csv"; // Modify this to include all file names
            os.write(fileNames.getBytes());

            // Continue querying until user types "EXIT"
            String query = new String();
            while (!query.equalsIgnoreCase("EXIT")){
                System.out.print("Send a district name to the server to see stats (Type 'EXIT' to quit): ");
                query = br.readLine();// Byte lar okunur
                os.write(query.getBytes()); // çıkışa yazılır ve servera gönderilir.

                // Receive and display server's response
                byte[] resultBytes = new byte[1024];
                int resultLength = is.read(resultBytes);
                String result = new String(resultBytes, 0, resultLength);
                System.out.println("Server result: " + result);
            }


            // Close connections
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
