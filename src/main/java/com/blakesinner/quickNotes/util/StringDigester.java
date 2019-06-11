package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StringDigester {

    private static final Logger LOGGER = LogManager.getLogger(StringDigester.class);

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
