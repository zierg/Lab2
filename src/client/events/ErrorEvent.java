package client.events;

import java.util.EventObject;

public class ErrorEvent extends EventObject {
    private String message;
    
    public ErrorEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}