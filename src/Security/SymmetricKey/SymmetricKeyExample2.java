package Security.SymmetricKey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricKeyExample2 {

    public static void main(String[] args) {
        try {
            // Generate a symmetric key
            SecretKey secretKey = generateSymmetricKey();
            // Convert the secret key to bytes
            byte[] keyBytes = secretKey.getEncoded();
            // Create a secret key specification from the key bytes
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            // Create a cipher instance for encryption and decryption
            Cipher cipher = Cipher.getInstance("AES");
            // Prepare the cipher for encryption using the secret key
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // The plaintext message to be encrypted
            String plaintext = "Hello, World!";
            // Convert the plaintext message to bytes
            byte[] plaintextBytes = plaintext.getBytes("UTF-8");
            // Encrypt the plaintext message
            byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
            // Print the encrypted ciphertext
            System.out.println("Encrypted ciphertext: " + new String(ciphertextBytes, "UTF-8"));
            // Prepare the cipher for decryption using the same secret key
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // Decrypt the ciphertext message
            byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);
            // Convert the decrypted bytes to plaintext
            String decryptedText = new String(decryptedBytes, "UTF-8");
            // Print the decrypted plaintext
            System.out.println("Decrypted plaintext: " + decryptedText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a symmetric key
    private static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(128, secureRandom);
        return keyGenerator.generateKey();
    }
}

