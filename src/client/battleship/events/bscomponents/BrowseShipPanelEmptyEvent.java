package client.battleship.events.bscomponents;

import java.util.EventObject;

public class BrowseShipPanelEmptyEvent extends EventObject {
    private String message;
    
    public BrowseShipPanelEmptyEvent(Object source) {
        super(source);
    }
    
    public BrowseShipPanelEmptyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}