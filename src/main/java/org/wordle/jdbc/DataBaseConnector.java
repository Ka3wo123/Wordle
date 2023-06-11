package org.wordle.jdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wordle.api.Statistics;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

/***
 * It has methods necessary for manipulating user's data between app and database.
 */
public class DataBaseConnector {

    private static Connection connection = null;

    /***
     * Establishes connection with database.
     * <br>
     * Uses data from properties file.
     */
    public static Connection connectToDB() {

        if (connection != null) {
            return connection;
        }

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
            System.out.println("""
                    ********************************
                    *          DB CONNECTED        *
                    ********************************
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /***
     * Fetches statistics for user.
     * @return ObservableList to use it in TableView
     * @see org.wordle.controllers.UserStatisticsController
     */
    public static ObservableList<Statistics> getStatisticsByUsername(String username) {
        connection = connectToDB();
        if (connection == null) {
            System.out.println("Connection to database failed");
            return null;
        }
        ObservableList<Statistics> statistics = FXCollections.observableArrayList();
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

    /***
     * Add new user to database.
     * @return True if operation executed successfully.
     */
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
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }

        return true;
    }

    /***
     * Checks if provided username already exists in database.
     * @see org.wordle.controllers.LoginController
     * @return True if username exists.
     */
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

                for (byte b : byteArray) {
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

    /***
     * Saves statistics for user.
     * @return True if operation executed successfully.
     */
    public static boolean saveStatisticsForUser(String username, String guessedWord, int attempts) {
        connection = connectToDB();

        if (connection == null) {
            System.out.println("Connection to database failed");
            return false;
        }

        int id;

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT w.id
                    FROM wordle.words w
                    WHERE w.word = ?
                    """);
            ps.setString(1, guessedWord);

            ResultSet resultId = ps.executeQuery();
            resultId.next();
            id = resultId.getInt("id");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO wordle.statistics VALUES
                    (DEFAULT, ?, ?, now(), ?)
                    """);
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setInt(3, attempts);

            ps.executeUpdate();

            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }

    /***
     * Draws word from database.
     * <br>
     * <b>Drawing random ID should be done with COUNT(*) in db</b>
     */
    public static String getRandomWord() {
        connection = connectToDB();

        if (connection == null) {
            System.out.println("Connection to database failed");
            return null;
        }

        Random random = new Random();
        int randomId = random.nextInt(120) + 1;
        String word = null;

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT w.word
                    FROM wordle.words AS w
                    WHERE w.id = ?""");
            ps.setInt(1, randomId);

            ResultSet result = ps.executeQuery();
            result.next();
            word = result.getString("word");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return word;
    }

    /***
     * Search and checks if word exists in database.
     * <br>
     * <b>Due to manual words inserting it has restrictive amount of words</b>
     */
    public static boolean checkIfWordInPoll(String word) {
        connection = connectToDB();

        if (connection == null) {
            System.out.println("Connection to database failed");
            return false;
        }

        boolean wordExists;

        try {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT EXISTS(SELECT 1
                                  FROM wordle.words
                                  WHERE word = ?
                                  )
                    """);
            ps.setString(1, word);
            ResultSet result = ps.executeQuery();
            result.next();
            wordExists = result.getBoolean(1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }

        return wordExists;
    }
}