package client.battleship.events.bscomponents;

import java.util.EventObject;

public class BattlefieldActionEvent extends EventObject {
    private String message;
    
    public BattlefieldActionEvent(Object source) {
        super(source);
    }
    
    public BattlefieldActionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
