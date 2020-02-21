package messagesfx;

import javafx.application.Application;
import javafx.stage.Stage;
import messagesfx.views.ScreenLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ScreenLoader.loadScreen("LoginView.fxml", primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}