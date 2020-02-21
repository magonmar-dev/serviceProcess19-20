package messagesfx.models.responses;

import messagesfx.models.Message;

import java.util.List;

public class GetMessagesResponse extends OkResponse {

    private List<Message> messages;

    public List<Message> getMessages() { return messages; }
}
