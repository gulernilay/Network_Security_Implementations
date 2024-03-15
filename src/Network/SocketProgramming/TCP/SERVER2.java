package Network.SocketProgramming.TCP;

import java.io.*;
import java.net.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;

        ServerSocket welcomeSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            // Karakter halinde verileri okucan
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            clientSentence = inFromClient.readLine();

            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence); // outToClient.writeBytes(capitalizedSentence);
        }
    }
}
