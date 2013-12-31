package client.battleship.events;

import java.util.EventObject;

public class PlayerIsPreparedEvent extends EventObject {
    private String message;
    
    public PlayerIsPreparedEvent(Object source) {
        super(source);
    }
    
    public PlayerIsPreparedEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
