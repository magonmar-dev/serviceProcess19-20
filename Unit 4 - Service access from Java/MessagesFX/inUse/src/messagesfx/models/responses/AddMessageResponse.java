package messagesfx.models.responses;

import messagesfx.models.Message;

public class AddMessageResponse extends OkResponse {

    private Message message;

    public Message getMessage() { return message; }
}
