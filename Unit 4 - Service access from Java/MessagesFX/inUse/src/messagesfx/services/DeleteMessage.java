package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.responses.DeleteMessageResponse;

public class DeleteMessage extends Service<DeleteMessageResponse> {

    String messageId;

    public DeleteMessage(String messageId) {
        this.messageId = messageId;
    }

    @Override
    protected Task<DeleteMessageResponse> createTask() {
        return new Task<>() {
            @Override
            protected DeleteMessageResponse call() throws Exception {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER +"/messages/" + messageId,
                        null, "DELETE");

                DeleteMessageResponse resp = gson.fromJson(json, DeleteMessageResponse.class);
                return resp;
            }
        };
    }
}
