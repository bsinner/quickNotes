package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.URISyntaxException;

/**
 * Load a secret key.
 *
 * @author bsinner
 */
public class KeyLoader {


    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Get the byte array of a key string.
     *
     * @param path the key location
     * @return     the key bytes
     */
    public byte[] getKeyBytes(String path) {
        return getKey(path).getBytes();
    }

    /**
     * Gets secret key.
     *
     * @param path the key location
     * @return     the key string
     */
    public String getKey(String path) {
        String results = "";

        try (BufferedReader br = new BufferedReader(new FileReader(getFile(path)))) {
            results = br.readLine();
        } catch (FileNotFoundException fnf) {
            logger.trace("FileNotFoundException Occurred");
        } catch (IOException ioe) {
            logger.trace("IOException Occurred");
        }

        return results;
    }

    /**
     * Get the file object at the path.
     *
     * @param path the file location
     * @return     the file object
     */
    private File getFile(String path) {
        File file = null;

        try {
            file = new File(this.getClass().getResource(path).toURI());
        } catch (URISyntaxException use) {
            logger.trace(use);
        }

        return file;
    }
}
