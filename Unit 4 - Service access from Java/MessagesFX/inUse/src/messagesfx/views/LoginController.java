package messagesfx.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import messagesfx.models.User;
import messagesfx.models.responses.LoginResponse;
import messagesfx.services.Login;
import messagesfx.services.ServiceUtils;

import java.io.IOException;

public class LoginController {

    public static User currentUser;

    @FXML private TextField tbUser;
    @FXML private PasswordField tbPass;
    @FXML private Button btLogin;
    @FXML private Hyperlink hyperlink;
    @FXML private Label lblError;

    @FXML
    void loginAction(ActionEvent event) {
        if(tbUser.getText().trim().isEmpty() || tbPass.getText().trim().isEmpty()) {
            lblError.setVisible(true);
            lblError.setText("Empty fields");
        } else {
            lblError.setVisible(false);
            User user = new User(tbUser.getText(), tbPass.getText());
            Login login = new Login(user);

            login.start();
            login.setOnSucceeded((e) -> {
                LoginResponse resp = login.getValue();
                if(!resp.isOk()) {
                    lblError.setVisible(true);
                    lblError.setText(resp.getError());
                } else {
                    ServiceUtils.setToken(resp.getToken());
                    currentUser = new User(resp.getName(), tbPass.getText(), resp.getImage());
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.close();
                    try {
                        ScreenLoader.loadScreen("MessageListView.fxml",
                                (Stage) ((Node) event.getSource()).getScene().getWindow());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    void goToRegister(ActionEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
        ScreenLoader.loadScreen("RegisterView.fxml", window);
    }
}
