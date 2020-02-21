package bingofxclient;

import javafx.scene.control.Alert;

public class MessageUtils {

    public static void showMessage(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message);
        a.setTitle(title);
        a.setContentText(message);
        a.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message);
        a.setTitle(title);
        a.setContentText(message);
        a.showAndWait();
    }
}
