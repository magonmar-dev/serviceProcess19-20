package flightsfx;

import flightsfx.utils.FileUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlightsFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMainView.fxml"));
        primaryStage.setTitle("FlightsFX");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        FileUtils.saveFlights(FXMLMainViewController.getFlights());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
