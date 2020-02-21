package bingofxclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class FXMLMainViewController {

    @FXML private TextField tbAddress;
    @FXML private TextField tbPort;
    @FXML private Button btConnect;
    @FXML private Label lblNumber1;
    @FXML private Label lblNumber2;
    @FXML private Label lblNumber3;
    @FXML private Label lblNumber4;
    @FXML private Label lblNumber5;
    @FXML private Label lblGameInfo;
    @FXML private Label lblCurrentNumber;

    private List<Label> labels = new ArrayList<>();

    @FXML
    void initialize() {
        labels.add(lblNumber1);
        labels.add(lblNumber2);
        labels.add(lblNumber3);
        labels.add(lblNumber4);
        labels.add(lblNumber5);
    }

    @FXML
    void connect(ActionEvent event) {

        if(tbAddress.getText().isBlank()) {
            MessageUtils.showMessage("Empty adress field",
                    "Please enter the server address to connect");
        } else if(tbPort.getText().isBlank()) {
            MessageUtils.showMessage("Empty port field",
                    "Please enter the server port to connect");
        } else {
            for (int i = 0; i < labels.size(); i++) {
                labels.get(i).setStyle("-fx-background-color:#cecece");
                labels.get(i).setText("");
            }
            btConnect.setDisable(true);
            lblGameInfo.setText("Waiting for other players...");
            lblCurrentNumber.setText("");

            ServerConnection t = new ServerConnection(tbAddress.getText(), Integer.parseInt(tbPort.getText()),
                    labels, lblGameInfo, btConnect, lblCurrentNumber);
            t.start();
        }
    }
}