package com.blakesinner.quicknotes.test.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to run sql statements as part of set up or tear down in the unit tests.
 */
public class DatabaseUtility {
    private final Logger logger = LogManager.getLogger(this.getClass());

    //TODO add hard-coded values to props file
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://localhost:3306/notes";

    static final String USER = "root";

    static final String PASS = "student";


    /**
     * Run the sql.
     *
     * @param sqlFile the sql file to be read and executed line by line
     */
    public void runSQL(String sqlFile) {
        Connection conn = null;
        Statement stmt = null;
        try (BufferedReader br = new BufferedReader(new FileReader(sqlFile))) {

            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();

            String sql = "";

            while (br.ready()) {
                sql += br.readLine();
            }

            stmt.executeUpdate(sql);

        } catch (FileNotFoundException fe) {
            logger.error(fe);
        } catch (SQLException se) {
            logger.error(se);
        } catch (Exception e) {
            logger.error(e);
        }

    }
}
