package Network.SocketProgramming.TCP;
import java.net.*;
import java.io.*;

public class TCP_SERVER {
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket socket= new ServerSocket(5000);
            System.out.println("Server started. Listening on port 5000...");
            while(true){
                Socket open= socket.accept();
                System.out.println("Client connected to server is  " + open.getInetAddress().getHostAddress() );
            // Send message :
                String message=" Hi, I know you";
                OutputStream os= open.getOutputStream();
                os.write(message.getBytes());

            // receive message :
                InputStream is = open.getInputStream();
                byte[] alınan = new byte[1024];
                int length= is.read(alınan);
                String response= new String(alınan,0,length);
                System.out.println("Client response: " + response);

                // Close the connection:

                open.close();
                System.out.println("Client disconnected.");

            }
        }
        catch(Exception e){
            e.printStackTrace();


        }



        }
    }
/*
try{
            ServerSocket socket= new ServerSocket(5000);
            System.out.println("Server started. Listening on port 5000...");
            while (true){
                 Socket socket1= socket.accept();// açtığın server soketinde accept socketi yaratıcaksın
                 System.out.println("Client connected: " + socket1.getInetAddress().getHostAddress()); // Ipsinin host kısmını alır
                 System.out.println(socket1); //Socket[addr=/127.0.0.1,port=11815,localport=5000]


                 //Read input from client
                BufferedReader br= new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                String input=br.readLine();
                System.out.println("Client says:" + input);

                // Send response:
                OutputStream os= socket1.getOutputStream();
                PrintWriter pw= new PrintWriter(os,true);
                pw.println("Hello World");

                // Close socket
                socket1.close();
                System.out.println("Client disconnected.");
                }

        }
        catch(Exception e){
            e.printStackTrace();

        }
 */
