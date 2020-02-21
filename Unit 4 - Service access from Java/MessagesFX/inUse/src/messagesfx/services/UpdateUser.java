package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.responses.UpdateUserResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class UpdateUser extends Service<UpdateUserResponse> {

    File image;

    public UpdateUser(File image) { this.image = image; }

    private String encodePhotoBase64() {
        byte[] bytes;
        String data = "";
        try {
            bytes = Files.readAllBytes(image.toPath());
            data = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            System.err.println("Error getting bytes from " + image.toPath().toString());
        }
        return data;
    }

    @Override
    protected Task<UpdateUserResponse> createTask() {
        return new Task<>() {
            @Override
            protected UpdateUserResponse call() {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                         ServiceUtils.SERVER + "/users", gson.toJson(encodePhotoBase64()), "PUT");
                UpdateUserResponse resp = gson.fromJson(json, UpdateUserResponse.class);
                return resp;
            }
        };
    }
}
