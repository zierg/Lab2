package network;

import java.io.IOException;

public interface Messenger {
    public Message getMessage() throws IOException;
    public void sendMessage(Message message) throws IOException;
}
