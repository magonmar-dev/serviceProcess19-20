package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.responses.GetMessagesResponse;

public class GetMessages extends Service<GetMessagesResponse> {

    @Override
    protected Task<GetMessagesResponse> createTask() {
        return new Task<>() {
            @Override
            protected GetMessagesResponse call() {
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER +"/messages", null, "GET");
                Gson gson = new Gson();
                GetMessagesResponse comps = gson.fromJson(json, GetMessagesResponse.class);
                return comps;
            }
        };
    }
}
