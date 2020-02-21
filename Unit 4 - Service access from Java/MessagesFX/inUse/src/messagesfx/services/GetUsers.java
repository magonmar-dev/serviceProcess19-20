package messagesfx.services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import messagesfx.models.responses.GetUsersResponse;

public class GetUsers extends Service<GetUsersResponse> {

    @Override
    protected Task<GetUsersResponse> createTask() {
        return new Task<>() {
            @Override
            protected GetUsersResponse call() {
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER +"/users", null, "GET");
                Gson gson = new Gson();
                GetUsersResponse comps = gson.fromJson(json, GetUsersResponse.class);
                return comps;
            }
        };
    }
}
