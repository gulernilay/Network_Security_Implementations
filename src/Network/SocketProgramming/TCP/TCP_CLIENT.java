package Network.SocketProgramming.TCP;

import java.net.*;
import java.io.*;


public class TCP_CLIENT {
    public static void main(String[] args) throws Exception{

        try{
            Socket socket1= new Socket("localhost",5000);
            OutputStream os= socket1.getOutputStream();
            InputStream is= socket1.getInputStream();

            // Send a message to client:
            String message= "Hello Worlds";
            os.write(message.getBytes());

            // Receive an message: Byte halidnde alıcağın için
            byte[] array= new byte[1024];
            int lenght = is.read(array);
            String response= new String(array,0,lenght);
            System.out.println("Server response: " + response);

            // Close the socket

            socket1.close();
            os.close();
            is.close();

        }
        catch(Exception e){
            e.printStackTrace();

        }


    }
}

