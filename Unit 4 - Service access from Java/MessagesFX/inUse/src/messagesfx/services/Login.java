package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.User;
import messagesfx.models.responses.LoginResponse;

public class Login extends Service<LoginResponse> {

    User userClient;

    public Login(User userClient) {
        this.userClient = userClient;
    }

    @Override
    protected Task<LoginResponse> createTask() {
        return new Task<>() {
            @Override
            protected LoginResponse call() {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER + "/login",
                        gson.toJson(userClient), "POST");
                LoginResponse resp = gson.fromJson(json, LoginResponse.class);
                return resp;
            }
        };
    }
}
