package client.events;

import java.util.EventObject;
import network.User;

public class invitedToPlayEvent extends EventObject {
    private User invitor;
    
    public invitedToPlayEvent(Object source, User invitor) {
        super(source);
        this.invitor = invitor;
    }
    
    public User getInvitor() {
        return invitor;
    }
}
