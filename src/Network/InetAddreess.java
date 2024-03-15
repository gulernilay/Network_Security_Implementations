package Network;
import java.net.*;
import java.io.*;
import java.sql.SQLOutput;

public class InetAddreess { // hazır class - Ip çözümleme veya Domain name bulma
    public static void main(String[] args) throws Exception{
        InetAddress address;

        try{
            address=InetAddress.getByName("www.turkiye.gov.tr");
            System.out.println(address.getHostAddress());
            address=InetAddress.getByName("94.55.118.33");
            System.out.println(address.getHostName());

            InetAddress localhost=InetAddress.getLocalHost();
            System.out.println(localhost);
            String IP=localhost.getHostAddress(); // my IP
            System.out.println(IP);
            String hostname=localhost.getHostName(); // my Host name
            System.out.println(hostname);

            InetAddress[] allIP= InetAddress.getAllByName("www.javatpoint.com");
            for (InetAddress address1 : allIP) {
                System.out.println(address1.getHostAddress());
            }




        }
        catch(Exception e){
            e.printStackTrace();


        }

        /*
        Bu kod parçası, "www.javatpoint.com" etki alanına ait tüm IP adreslerini elde etmek için kullanılır. InetAddress.getAllByName() metodu, belirtilen etki alanı için tüm IP adreslerini döndüren bir dizi InetAddress nesnesi döndürür.
        Daha sonra, elde edilen IP adreslerini dizi üzerinden döngü ile alırız. Her bir InetAddress nesnesi için getHostAddress() yöntemini kullanarak IP adresini elde ederiz ve bu IP adresini ekrana yazdırırız. Yani, "www.javatpoint.com" etki alanına ait tüm IP adreslerini alıp, bu IP adreslerini ekrana yazdırıyoruz.
         */
    }
}
