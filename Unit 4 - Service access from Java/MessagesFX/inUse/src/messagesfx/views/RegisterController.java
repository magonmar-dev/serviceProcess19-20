package messagesfx.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messagesfx.models.User;
import messagesfx.models.responses.RegisterResponse;
import messagesfx.services.Register;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class RegisterController {

    @FXML private TextField tbUser;
    @FXML private PasswordField tbPass;
    @FXML private PasswordField tbRpass;
    @FXML private Button btCancel;
    @FXML private Button btRegister;
    @FXML private Button btSelectImage;
    @FXML private ImageView imageView;
    @FXML private Label lblError;

    File img = null;

    @FXML
    void cancelAction(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    @FXML
    void registerAction(ActionEvent event) {

        if(tbUser.getText().trim().isEmpty() || tbPass.getText().trim().isEmpty() ||
                tbRpass.getText().trim().isEmpty()) {
            lblError.setVisible(true);
            lblError.setText("Empty fields");
            return;
        }

        if(!tbPass.getText().trim().equals(tbRpass.getText().trim())) {
            lblError.setVisible(true);
            lblError.setText("Passwords don’t match");
            return;
        }

        if(img == null) {
            lblError.setVisible(true);
            lblError.setText("You must choose an image!");
            return;
        }

        lblError.setVisible(false);
        User user = new User(tbUser.getText(), tbPass.getText(), encodePhotoBase64());
        Register register = new Register(user);

        register.start();
        register.setOnSucceeded((e) -> {
            RegisterResponse resp = register.getValue();
            if(resp.getError() == null) {
                lblError.setVisible(true);
                lblError.setText(resp.getError());
            } else {
                btRegister.setDisable(true);
                img = null;
                tbUser.clear();
                tbPass.clear();
                tbRpass.clear();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Register completed");
                alert.setHeaderText("You have successfully register");
                alert.setContentText("You will be redirected to the login page");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        ScreenLoader.loadScreen("LoginView.fxml",
                                (Stage) ((Node) event.getSource()).getScene().getWindow());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                }
            }
        });
    }

    @FXML
    void selectImageAction(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG images",
                        Arrays.asList("*.jpg", "*.jpeg")));
        img = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(img != null) {
            imageView.setPreserveRatio(true);
            imageView.setImage(new Image(new FileInputStream(img)));
        }
    }

    private String encodePhotoBase64() {
        byte[] bytes;
        String data = "";
        try {
            bytes = Files.readAllBytes(img.toPath());
            data = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            System.err.println("Error getting bytes from " + img.toPath().toString());
        }
        return data;
    }
}
