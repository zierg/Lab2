package client.battleship.events;

import java.util.EventObject;

public class GameOverEvent extends EventObject {
    private String message;
    
    public GameOverEvent(Object source) {
        super(source);
    }
    
    public GameOverEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
