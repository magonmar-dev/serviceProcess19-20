package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.Message;
import messagesfx.models.responses.AddMessageResponse;

public class AddMessage extends Service<AddMessageResponse> {

    Message newMessage;
    String userId;

    public AddMessage(Message newMessage, String userId) {
        this.newMessage = newMessage;
        this.userId = userId;
    }

    @Override
    protected Task<AddMessageResponse> createTask() {
        return new Task<>() {
            @Override
            protected AddMessageResponse call() throws Exception {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER + "/messages/" + userId,
                        gson.toJson(newMessage), "POST");
                AddMessageResponse resp = gson.fromJson(json, AddMessageResponse.class);
                return resp;
            }
        };
    }
}
