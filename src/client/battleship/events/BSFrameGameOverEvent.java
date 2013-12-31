package client.battleship.events;

import java.util.EventObject;

public class BSFrameGameOverEvent extends EventObject {
    private String message;
    
    public BSFrameGameOverEvent(Object source) {
        super(source);
    }
    
    public BSFrameGameOverEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
