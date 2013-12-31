package client.events;

import java.util.EventObject;

public class NetworkPlayerIsReadyEvent extends EventObject {
    private String message;
    
    public NetworkPlayerIsReadyEvent(Object source) {
        super(source);
    }
    
    public NetworkPlayerIsReadyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}