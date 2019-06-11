package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class for encrypting strings with SHA-2.
 *
 * Bytes to hex String method from:
 * https://www.baeldung.com/sha-256-hashing-java
 *
 * @author bsinner
 */
public class StringDigester {

    private static final Logger LOGGER = LogManager.getLogger(StringDigester.class);

    /**
     * Encrypt string with SHA-2.
     *
     * @param string the string to convert
     * @return       the converted string, or null if a no such algorithm exception occurred
     */
    public static String encrypt(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(string.getBytes(UTF_8));

            return toHex(bytes);
        } catch (NoSuchAlgorithmException nsae) {
            LOGGER.trace("Error: Password digest algorithm not found", nsae);
        }

        return null;
    }

    /**
     * Convert byte array to hex string.
     *
     * @param bytes the bytes to convert
     * @return      the hex string
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte character : bytes) {
            String converted = Integer.toHexString(0xff & character);

            if (converted.length() == 1) {
                sb.append(0);
            }

            sb.append(converted);
        }

        return sb.toString();
    }
}
