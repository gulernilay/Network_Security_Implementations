package Security.DigitalSignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public class VerifySignatures {
    @SuppressWarnings("unchecked")

    public static void main(String[] args) throws Exception{
        List<byte[]> list;

        // retrieves the byte arrays that contains the message and signature form the file.
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("SignStore/SignedData.txt"));
        list = (List<byte[]>) in.readObject();
        in.close();
        byte [] msg = list.get(0);
        byte [] signature = list.get(1);

        // retrieve the Public Key from a file.
        byte[] keyBytes = Files.readAllBytes(new File("KeyStore/publicKey").toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(spec);

        // Initialize the signature with the Public Key,
        // Updates the data to be verified and then verifies them using the signature
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(msg);

        Boolean verified = sig.verify(signature);

        System.out.println(verified ? "VERIFIED MESSAGE" +
                "\n----------------\n" + new String(msg) : "Could not verify the signature.");
    }
}
