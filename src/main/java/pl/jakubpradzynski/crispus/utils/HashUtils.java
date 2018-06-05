package pl.jakubpradzynski.crispus.utils;

import pl.jakubpradzynski.crispus.exceptions.HashGenerationException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utils-type class with helpful methods related to hash.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class HashUtils {

    /**
     * Method hash given message using given algorithm's name.
     * @param message - message which we want to hash
     * @param algorithm - algorithm used for hashing
     * @return String (message hash)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    private static String hashString(String message, String algorithm)
            throws HashGenerationException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashGenerationException(
                    "Could not generate hash from String", ex);
        }
    }

    /**
     * Method convert bytes array to hex string.
     * @param arrayBytes - bytes which we want to convert to string
     * @return String
     */
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * Method hash message in MD5.
     * @param message - message which we want to be hashed
     * @return String (hash)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    public static String generateMD5(String message) throws HashGenerationException {
        return hashString(message, "MD5");
    }

    /**
     * Method hash message in SHA1.
     * @param message - message which we want to be hashed
     * @return String (hash)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    public static String generateSHA1(String message) throws HashGenerationException {
        return hashString(message, "SHA-1");
    }

    /**
     * Method hash message in SHA256.
     * @param message - message which we want to be hashed
     * @return String (hash)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    public static String generateSHA256(String message) throws HashGenerationException {
        return hashString(message, "SHA-256");
    }

}
