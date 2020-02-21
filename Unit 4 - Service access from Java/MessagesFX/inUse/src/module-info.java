module MessagesFX {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.gson;
    requires java.sql;

    opens messagesfx;
    opens messagesfx.views;
    opens messagesfx.models;
}