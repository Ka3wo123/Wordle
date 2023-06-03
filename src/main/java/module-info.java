module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.wordle to javafx.fxml;
    exports org.wordle;
    exports org.wordle.controllers;
    opens org.wordle.controllers to javafx.fxml;
}