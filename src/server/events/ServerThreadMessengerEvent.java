package server.events;

import java.util.EventObject;
import network.Message;

public class ServerThreadMessengerEvent extends EventObject {
    
    private Message message;
    
    public ServerThreadMessengerEvent(Object source) {
        super(source);
    }
    
    public ServerThreadMessengerEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
    
    public Message getMessage() {
        return message;
    }
}

