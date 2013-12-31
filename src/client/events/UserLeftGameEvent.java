package client.events;

import java.util.EventObject;

public class UserLeftGameEvent extends EventObject {
    private String message;
    
    public UserLeftGameEvent(Object source) {
        super(source);
    }
    
    public UserLeftGameEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
