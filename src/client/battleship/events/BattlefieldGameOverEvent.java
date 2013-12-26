package client.battleship.events;

import java.util.EventObject;

public class BattlefieldGameOverEvent extends EventObject {
    private String message;
    
    public BattlefieldGameOverEvent(Object source) {
        super(source);
    }
    
    public BattlefieldGameOverEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}