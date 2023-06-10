package org.wordle.api;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/***
 * Singleton for StringProperty.
 */
public class PropertiesSingleton {
    private static final StringProperty properties = new SimpleStringProperty();

    public static StringProperty getProperties() {
        return properties;
    }
}
