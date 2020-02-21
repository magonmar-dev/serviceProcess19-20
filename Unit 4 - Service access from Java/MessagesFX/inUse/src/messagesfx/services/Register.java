package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.User;
import messagesfx.models.responses.RegisterResponse;

public class Register extends Service<RegisterResponse> {

    User newUser;

    public Register(User newUser) { this.newUser = newUser; }

    @Override
    protected Task<RegisterResponse> createTask() {
        return new Task<>() {
            @Override
            protected RegisterResponse call() {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER + "/register",
                        gson.toJson(newUser), "POST");
                RegisterResponse resp = gson.fromJson(json, RegisterResponse.class);
                return resp;
            }
        };
    }
}
