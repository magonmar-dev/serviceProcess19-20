package messagesfx.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenLoader {

    public static void loadScreen(String viewPath, Stage stage) throws IOException {
        Parent view = FXMLLoader.load(ScreenLoader.class.getResource(viewPath));
        Scene scene = new Scene(view);
        stage.hide();
        stage.setTitle("MessagesFX");
        stage.setScene(scene);
        stage.show();
    }
}
