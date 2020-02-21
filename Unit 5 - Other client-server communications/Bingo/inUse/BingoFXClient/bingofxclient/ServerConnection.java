package bingofxclient;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerConnection extends Thread {

    private String address;
    private int port;
    private List<Label> labels;
    private Label lblGameInfo;
    private Button btConnect;
    private Label lblCurrentNumber;

    public ServerConnection(String address, int port, List<Label> labels, Label lblGameInfo, Button btConnect, Label lblCurrentNumber) {
        this.address = address;
        this.port = port;
        this.labels = labels;
        this.lblGameInfo = lblGameInfo;
        this.btConnect = btConnect;
        this.lblCurrentNumber = lblCurrentNumber;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketOut.flush();
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

            List<Integer> resp = (List<Integer>) socketIn.readObject();
            Platform.runLater(() -> {
                lblGameInfo.setText("");
                for (int i = 0; i < resp.size(); i++) {
                    labels.get(i).setText(resp.get(i).toString());
                }
            });

            int result;
            do {
                result = socketIn.readInt();
                int tempResult = result;
                if (result != -1 && result != -2)
                    Platform.runLater(() -> lblCurrentNumber.setText("Current number: " + tempResult));
                if (resp.contains(result)) {
                    Platform.runLater(() -> {
                        for (int i = 0; i < resp.size(); i++) {
                            if (resp.get(i) == tempResult || tempResult == -2)
                                labels.get(i).setStyle("-fx-background-color:#00ff00");
                        }
                    });
                }
            } while (result != -1 && result != -2);

            socket.close();

            if (result == -2)
                Platform.runLater(() -> lblGameInfo.setText("You WIN!!"));
            else
                Platform.runLater(() -> lblGameInfo.setText("You LOSE"));

            sleep(2000);
            Platform.runLater(() -> btConnect.setDisable(false));
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            Platform.runLater(() -> {
                MessageUtils.showError("Error connecting", e.getMessage());
                btConnect.setDisable(false);
            });
        }
    }
}
