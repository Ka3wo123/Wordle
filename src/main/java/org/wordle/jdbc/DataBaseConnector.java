package org.wordle.jdbc;

import org.wordle.api.Statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

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
            System.out.println("Null jest kurwa");
            return null;
        }
        List<Statistics> statistics = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT s.game_id, w.word, au.username, s.date_of_game, s.attempts
                    FROM wordle.app_user au
                    natural join wordle.statistics s
                    inner join wordle.words w on w.id = s.guessed_word_id
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
}