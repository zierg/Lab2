package client.events;

import java.util.EventObject;

public class NetworkGameOverEvent extends EventObject {
    private String message;
    
    public NetworkGameOverEvent(Object source) {
        super(source);
    }
    
    public NetworkGameOverEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}