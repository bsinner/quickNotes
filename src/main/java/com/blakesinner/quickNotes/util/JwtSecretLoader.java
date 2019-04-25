package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.URISyntaxException;

public class JwtSecretLoader {

    private final String PATH = "/accessTokenPw.txt";
    private final Logger logger = LogManager.getLogger(this.getClass());

    public JwtSecretLoader() { }

    public String getSecret() {
        String results = "";

        try (BufferedReader br = new BufferedReader(new FileReader(getFile()))) {
            results = br.readLine();
        } catch (FileNotFoundException fnf) {
            logger.trace("FileNotFoundException Occurred");
        } catch (IOException ioe) {
            logger.trace("IOException Occurred");
        }

        return results;
    }

    private File getFile() {
        File file = null;

        try {
            file = new File(this.getClass().getResource(PATH).toURI());
        } catch (URISyntaxException use) {
            logger.trace("URISyntaxException Occurred");
        }

        return file;
    }
}
