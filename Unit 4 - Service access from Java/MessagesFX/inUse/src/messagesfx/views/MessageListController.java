package messagesfx.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import messagesfx.models.Message;
import messagesfx.models.User;
import messagesfx.models.responses.DeleteMessageResponse;
import messagesfx.models.responses.GetMessagesResponse;
import messagesfx.models.responses.GetUsersResponse;
import messagesfx.models.responses.UpdateUserResponse;
import messagesfx.services.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageListController {

    @FXML private Label lblActualUser;
    @FXML private Button btChangeImage;
    @FXML private Button btRefresh;
    @FXML private ImageView userImage;
    @FXML private Button btDeleteMessage;
    @FXML private Button btSelectImage;
    @FXML private Button btSend;
    @FXML private TextField tbText;
    @FXML private ImageView messageImage;

    @FXML private TableView<Message> tableMessages;
    @FXML private TableColumn<Message, String> colText;
    @FXML private TableColumn<Message, ImageView> colImage;
    @FXML private TableColumn<Message, String> colSent;
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, ImageView> colAvatar;
    @FXML private TableColumn<User, String> colName;

    static ObservableList<User> listUsers;
    static ObservableList<Message> listMessages;

    File imgUser = null;
    File imgMessage = null;

    public void initialize() {
        lblActualUser.setText(LoginController.currentUser.getName());
        userImage.setPreserveRatio(true);
        userImage.setImage(new Image(ServiceUtils.SERVER + "/" + LoginController.currentUser.getImage()));

        showMessages();
        showUsers();
    }

    private void showMessages() {
        List<Message> listM = new ArrayList<>();
        GetMessages getMess = new GetMessages();
        getMess.start();
        getMess.setOnSucceeded((e) -> {
            GetMessagesResponse resp = getMess.getValue();
            if(resp.isOk()) {
                listM.addAll(resp.getMessages());
                listMessages = FXCollections.observableArrayList(listM);
                colText.setCellValueFactory(new PropertyValueFactory<>("message"));
                colImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
                colSent.setCellValueFactory(new PropertyValueFactory<>("sent"));
                tableMessages.setItems(listMessages);
            }
        });
    }

    private void showUsers() {
        List<User> listU = new ArrayList<>();
        GetUsers getUsers = new GetUsers();
        getUsers.start();
        getUsers.setOnSucceeded((e) -> {
            GetUsersResponse resp = getUsers.getValue();
            if(resp.isOk()) {
                listU.addAll(resp.getUsers());
                listUsers = FXCollections.observableArrayList(listU);
                colAvatar.setCellValueFactory(new PropertyValueFactory<>("imageView"));
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                tableUsers.setItems(listUsers);
            }
        });
    }

    @FXML
    void changeUserImageAction(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG images",
                        Arrays.asList("*.jpg", "*.jpeg")));
        imgUser = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(imgUser != null) {
            userImage.setPreserveRatio(true);
            userImage.setImage(new Image(new FileInputStream(imgUser)));
        }

        UpdateUser update = new UpdateUser(imgUser);
        update.start();
        update.setOnSucceeded((e) -> {
            UpdateUserResponse resp = new UpdateUserResponse();
            if(resp.isOk()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update completed");
                alert.setHeaderText("You have successfully updated the new image");
                alert.show();
                showUsers();
            }
        });
    }

    @FXML
    void deleteMessageAction(ActionEvent event) {
        DeleteMessage delete = new DeleteMessage(tableMessages.getSelectionModel().getSelectedItem().get_id());
        delete.start();
        delete.setOnSucceeded((e) -> {
            DeleteMessageResponse resp = delete.getValue();
            if(resp.isOk()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete completed");
                alert.setHeaderText("You have successfully delete the message");
                alert.show();
                showMessages();
            }
        });
    }

    @FXML
    void enableDeleteBtn(MouseEvent event) {
        btDeleteMessage.setDisable(false);
    }

    @FXML
    void refreshAction(ActionEvent event) {
        showMessages();
    }

    @FXML
    void selectMessageImageAction(ActionEvent event) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG images",
                        Arrays.asList("*.jpg", "*.jpeg")));
        imgMessage = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(imgMessage != null) {
            messageImage.setPreserveRatio(true);
            messageImage.setImage(new Image(new FileInputStream(imgMessage)));
        }
    }

    @FXML
    void sendMessageAction(ActionEvent event) {
        if(tableUsers.getSelectionModel().getSelectedItem() == null || tbText.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty fields");
            alert.setHeaderText("You have to write the text and select a user");
            alert.show();
            return;
        }

        Message newMessage = null;
        String to = tableUsers.getSelectionModel().getSelectedItem().get_id();
        String message = tbText.getText();
        String image;
        Date date = new Date();
        String sent = new SimpleDateFormat("dd/MM/yyyy").format(date);

        if(imgMessage != null) {
            image = encodePhotoBase64();
            newMessage = new Message(to,message,image,sent);
        } else {
            newMessage = new Message(to,message,sent);
        }

        AddMessage addMessage = new AddMessage(newMessage,to);
        addMessage.start();
        addMessage.setOnSucceeded((e) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message sent");
            alert.setHeaderText("You have successfully sent the message");
            alert.show();
        });
    }

    private String encodePhotoBase64() {
        byte[] bytes;
        String data = "";
        try {
            bytes = Files.readAllBytes(imgMessage.toPath());
            data = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            System.err.println("Error getting bytes from " + imgMessage.toPath().toString());
        }
        return data;
    }
}
