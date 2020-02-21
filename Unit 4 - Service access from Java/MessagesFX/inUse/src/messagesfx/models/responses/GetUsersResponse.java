package messagesfx.models.responses;

import messagesfx.models.User;

import java.util.List;

public class GetUsersResponse extends OkResponse {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }
}
