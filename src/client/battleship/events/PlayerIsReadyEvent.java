package client.battleship.events;

import java.util.EventObject;

public class PlayerIsReadyEvent extends EventObject {
    private String message;
    
    public PlayerIsReadyEvent(Object source) {
        super(source);
    }
    
    public PlayerIsReadyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
