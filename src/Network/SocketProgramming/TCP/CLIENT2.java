package Network.SocketProgramming.TCP;
import java.io.*;
import java.net.*;
/*
kullanıcıdan bir metin girişi alır, bu girişi sunucuya gönderir ve sunucudan aldığı cevabı ekrana yazdırır.
 */


public class CLIENT2 {
    public static void main(String[] args) throws Exception {
        String sentence; // kullanıcıdan sentence al ve servera gönder, server cevabını ekrana yazdır.
        String modifiedSentence;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        sentence = inFromUser.readLine();
        // System.in = kullanıcı veri girişi için , BufferReader = karakter okuyucu , InputStream= byte akışının karaktere çevir

        Socket clientSocket = new Socket("localhost", 6789);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // dışarı yolluyosun
        outToServer.writeBytes(sentence + '\n');

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// byte cevabı oku karaktere çevir
        modifiedSentence = inFromServer.readLine();

        System.out.println("FROM SERVER: " + modifiedSentence);

        clientSocket.close();

    }
}
