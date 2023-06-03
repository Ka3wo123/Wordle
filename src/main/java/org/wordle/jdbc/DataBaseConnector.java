package org.wordle.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.Properties;

public class DataBaseConnector {

    private static Connection connection;

    public static Connection connectToDB() {
        try {
            Properties properties = new Properties();
            String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "application.properties";
            properties.load(new FileInputStream(path));

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(properties.getProperty("datasource.jdbc-url"), properties.getProperty("datasource.username"), properties.getProperty("datasource.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}