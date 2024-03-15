package Security.AsymmetricKeyEncryption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Key;
import javax.crypto.Cipher;

public class AsymmetricCryptography{
    public static void main(String[] args) throws Exception {
        // Generate Key Pair
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        // Get Public and Private Key from the Key Pair
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        // Original Message
        String message = "This is the message to be encrypted.";
        // Encrypt the message using the Public Key
        Cipher encryptionCipher = Cipher.getInstance("RSA"); // Instance of CIPHER
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey); // Initiate instance of CIPHER
        byte[] encryptedBytes = encryptionCipher.doFinal(message.getBytes());
        // Decrypt the message using the Private Key
        Cipher decryptionCipher = Cipher.getInstance("RSA"); // create instance of CIPHER
        decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey); // Initiate the instance of CIPHER in decryption mode
        byte[] decryptedBytes = decryptionCipher.doFinal(encryptedBytes);
        // Convert the decrypted bytes back to string
        String decryptedMessage = new String(decryptedBytes);
        // Print the original and decrypted messages
        System.out.println("Original Message: " + message);
        System.out.println("Encrypted Message: " + bytesToHexString(encryptedBytes));
        System.out.println("Decrypted Message: " + decryptedMessage);

    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}


