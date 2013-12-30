package client.battleship.events.bscomponents;

import java.util.EventObject;

public class BrowseShipPanelActionEvent extends EventObject {
    private String message;
    
    public BrowseShipPanelActionEvent(Object source) {
        super(source);
    }
    
    public BrowseShipPanelActionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
