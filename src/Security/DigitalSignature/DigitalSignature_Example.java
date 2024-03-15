package Security.DigitalSignature;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class DigitalSignature_Example {
    public static void main(String[] args) {
        String data = new String("This is SE375 SYSTEM PROGRAMMING.");
        //The list consists of the message and the signature.
        List<byte[]> list =new ArrayList<byte[]>();
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            list.add(data.getBytes());
            //retrieve the Private Key from a file
            byte[] keyBytes = Files.readAllBytes(new File("KeyStore/privateKey").toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey =  kf.generatePrivate(spec);
            //Sign the data using the private key
            rsa.initSign(privateKey);
            rsa.update(data.getBytes());
            byte[] signature = rsa.sign();
            list.add(signature);
            // write the (message, signature) to a file
            File f = new File("SignStore/SignedData.txt");
            f.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SignStore/SignedData.txt"));
            out.writeObject(list);
            out.close();
            System.out.println("Your file is ready.");
        } catch ( InvalidKeyException | NoSuchAlgorithmException |
                InvalidKeySpecException | SignatureException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
