module FlightsFX {
    requires javafx.fxml;
    requires javafx.controls;
    opens flightsfx;
    opens flightsfx.chartview;
    exports flightsfx.model;
}