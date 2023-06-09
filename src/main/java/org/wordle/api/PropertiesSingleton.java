package org.wordle.api;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PropertiesSingleton {
    private static StringProperty properties = new SimpleStringProperty();

    public static StringProperty getProperties() {
        return properties;
    }
}
