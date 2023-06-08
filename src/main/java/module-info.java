module org.wordle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    exports org.wordle;
    opens org.wordle to javafx.fxml;
    exports org.wordle.controllers;
    opens org.wordle.controllers to javafx.fxml;
    exports org.wordle.api;
    opens org.wordle.api to javafx.fxml;
}