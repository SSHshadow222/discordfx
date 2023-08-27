package com.example.discordfx.utils.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String CONN_FAILED_MSG = "Failed to connect to the database";
    private static Properties jdbcProps = null;
    public static Connection instance;

    public static Connection getConnection() {
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            } 
        }
        catch (SQLException e) {
            throw new RuntimeException(CONN_FAILED_MSG);
        }
        
        return instance;
    }
    
    private static Connection getNewConnection() {
        grantProperties();

        String url = jdbcProps.getProperty("jdbc.url");
        Connection conn;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(CONN_FAILED_MSG);
        }

        return conn;
    }
    
    private static void grantProperties() {
        if (jdbcProps == null) {
            jdbcProps = new Properties();
            
            try {
                jdbcProps.load(new FileReader("db.config"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
