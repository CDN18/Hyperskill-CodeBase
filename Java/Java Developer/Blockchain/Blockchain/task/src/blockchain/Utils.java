package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import static blockchain.Config.*;

public class Utils {
    /* Applies Sha256 to a string and returns a hash. */
    public static String Sha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /* Keypair Management */
    public static KeyPairGenerator keyPairAgent;
    public static Signature verifier;

    public static Signature getSigner(String algorithm) {
        Signature signer = null;
        try {
            signer = Signature.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Falling back to SHA256withRSA...");
            try {
                keyPairAgent = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
        return signer;
    }

    static {
        try {
            keyPairAgent = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM);
            keyPairAgent.initialize(KEY_SIZE);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Falling back to RSA...");
            try {
                keyPairAgent = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Falling back to SHA256withRSA...");
            try {
                verifier = Signature.getInstance("SHA256withRSA");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
