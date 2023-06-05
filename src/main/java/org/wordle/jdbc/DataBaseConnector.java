package org.wordle.jdbc;

import org.wordle.api.Statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DataBaseConnector {

    private static Connection connection;

    public static Connection connectToDB() {
        String propertiesFilePath = "src/main/resources/app.properties";

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(propertiesFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {


            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    properties.getProperty("datasource.jdbc-url"),
                    properties.getProperty("datasource.username"),
                    properties.getProperty("datasource.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static List<Statistics> getStatisticsByUsername(String username) {
        connection = connectToDB();
        if(connection == null) {
            System.out.println("Connection to database failed");
            return null;
        }
        List<Statistics> statistics = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT s.game_id, w.word, au.username, s.date_of_game, s.attempts
                    FROM wordle.app_user au
                    NATURAL JOIN wordle.statistics s
                    INNER JOIN wordle.words w on w.id = s.guessed_word_id
                    WHERE s.username = ?
                    """);
            ps.setString(1, username);
            ResultSet resultStatistics = ps.executeQuery();


            while (resultStatistics.next()) {
                statistics.add(new Statistics(
                        resultStatistics.getInt("game_id"),
                        resultStatistics.getString("word"),
                        resultStatistics.getString("username"),
                        resultStatistics.getTimestamp("date_of_game").toLocalDateTime(),
                        resultStatistics.getInt("attempts")));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return statistics;
    }

    public static boolean saveNewUser(String username, String password) {
        connection = connectToDB();

        if (connection == null) {
            System.out.println("Connection to database failed");
            return false;
        }

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO wordle.app_user
                    VALUES (?, ?)
                    """);
            ps.setString(1,username);
            ps.setString(2,password);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean validateCredentials(String username, String password) {
        connection = connectToDB();

        if (connection == null) {
            System.out.println("Connection to database failed");
            return false;
        }

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT EXISTS(SELECT 1
                                  FROM wordle.app_user
                                  WHERE username = ?
                                 )
                    """);
            ps.setString(1, username);

            ResultSet result = ps.executeQuery();
            result.next();

            if (result.getBoolean(1)) {
                ps = connection.prepareStatement("""
                        SELECT au.password
                        FROM wordle.app_user AS au
                        WHERE au.username = ?
                        """);
                ps.setString(1, username);

                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                String passwordDB = resultSet.getString(1);

                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                byte[] byteArray = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();

                for(byte b : byteArray) {
                    hexString.append(String.format("%02x", b));
                }
                String hashedPassword = hexString.insert(0, "\\x").toString();

                return passwordDB.equals(hashedPassword);
            } else {
                return false;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return false;

    }
}