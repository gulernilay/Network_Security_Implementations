package Security.MessageDigest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigest_Example{

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String password = "123456";
        String password = "The Quick Brown Fox Jumps Over The Lazy Dog";
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        // SHA-256 için denediğinde ise hashed form will be longer. (32 bytes)
        // more secure ,more diffucult to understand relation
        byte[] hashInBytes = md.digest(password.getBytes());
        // bytes to hex  : print on the screen
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println("Size in bytes:" + hashInBytes.length + "\n" + sb.toString());
        // SECOND PART: UPDATE
        String name = "John Doe";
        byte[] data1 = password.getBytes();
        byte[] data2 = name.getBytes();
        //       MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(data1);
        messageDigest.update(data2);
        hashInBytes = messageDigest.digest();
        sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println("Uses update()." + "Size in bytes:" + hashInBytes.length + "\n" + sb.toString());
    }

}
// bir stringe direkt digest uyguladığınla , önce update sonra digest uyguladmanın sonucu farklı çıkacaktır.
// 1 harf bile değişse hash value komple değişiyor.
// upuuzun bir name ile kısa bir name in hash halleri fixed size 32 bytes, 64 bytes

