package com.blakesinner.quickNotes.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Load a properties file.
 *
 * @author bsinner
 */
public class PropLoader {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Load the passed in properties file.
     *
     * @param path the properties file path, absolute path not needed,
     *             /file.properties will work
     * @return     the properties object
     */
    public Properties load(String path) {
        Properties props = new Properties();

        try {
            props.load(this.getClass().getResourceAsStream(path));
        } catch (IOException ioe) {
            logger.trace("An I/O Exception occurred while loading " + path, ioe);
        }

        return props;
    }

}
