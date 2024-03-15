package Security.SymmetricKey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;  // encryption algorithm
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricKeyExample {

    public static String printBytes(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X:", b));
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        String msg = "This is SE375 SYSTEM PROGRAMMING."; // plain text
        // Plain text - ecrypted message- decrypted message ı byte kümesi ile ifade edicez.
        byte[] plain_text;
        byte[] encrypted_text;
        byte[] decrypted_text;
        Cipher cipher;  // Cipher text = encrypted text

        try {
            plain_text = msg.getBytes("UTF-8");   // byte halini alıyoruz.
            System.out.println("Original data:" + msg);
            System.out.println(printBytes(plain_text)); // original data nın byte hali

            // STEP 1. Generate the Keys
            KeyGenerator keyGen = KeyGenerator.getInstance("AES"); //CBC , PKCS5Padding
            SecureRandom secureRandom = new SecureRandom(); // random key üretilirsin
            keyGen.init(256, secureRandom); // you must initialize keyGen instance
            SecretKey secretKey = keyGen.generateKey();    // generate key


            // length of the key must be 128 , 192 or 256 bits (16, 24, 32 byte)
//			byte [] key = "!'^+Pass@4meword".getBytes("UTF-8");
//			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            // STEP 2. Get a Cipher instance and initialize it for encryption using secret key
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // STEP 4. Encrypt plaintext
            encrypted_text = cipher.doFinal(plain_text);  //cipher.doFinal() is used for single part encryption and decryption
            // cipher.update() is used ofr encryption of multiple blocks of data
            // bu hali ile decrypte edersek hata almış oluruz  System.out.println("Encrypted data :" + encrypted_text);
            System.out.println("Encrypted data :" + printBytes(encrypted_text));

            // STEP 5. Decryption modunu initialize et ve decrypte et.
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decrypted_text = cipher.doFinal(encrypted_text);
            // System.out.println("Decrypted data:" + decrypted_text);
            System.out.println("Decrypted data:" + printBytes(decrypted_text));

            if (java.util.Arrays.equals(decrypted_text, plain_text)) {
                System.out.println("Obtained the original text: " + new String(decrypted_text));
            } //Bu kod bloğu, şifre çözülmüş metni (decrypted_text) orijinal metinle (plain_text) karşılaştırır ve eşitlik durumunda orijinal metni ekrana yazdırır.
        } catch (UnsupportedEncodingException ex) { //Şifreleme ve şifre çözme işlemlerinde karakter kodlaması (encoding) ile ilgili bir sorun oluşursa
            System.err.println("Couldn't create key: " + ex.getMessage());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) { //Belirtilen şifreleme algoritması veya şifreleme modu bulunamazsa bu istisna durumu tetiklenir.
            System.err.println(e.getMessage());
        }  catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) { //Şifreleme veya şifre çözme işlemleri sırasında geçersiz bir anahtar, hatalı blok boyutu veya hatalı dolgu (padding) durumları oluşursa bu istisna durumları tetiklenir
            System.err.println(e.getMessage());
        }
    }
}


/*
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public class SymmetricEncryptionExample {
    public static void main(String[] args) {
        try {
            // Generate a symmetric key
            SecretKey secretKey = generateSymmetricKey();

            // Convert the secret key to bytes
            byte[] keyBytes = secretKey.getEncoded();

            // Create a secret key specification from the key bytes
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // Create a cipher instance for encryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // The plaintext message to be encrypted
            String plaintext = "Hello, World!";

            // Convert the plaintext message to bytes
            byte[] plaintextBytes = plaintext.getBytes("UTF-8");

            // Encrypt the plaintext message
            byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

            // Print the encrypted ciphertext
            System.out.println("Encrypted ciphertext: " + new String(encryptedBytes, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate a symmetric key
    private static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        return keyGenerator.generateKey();
    }
}









 */