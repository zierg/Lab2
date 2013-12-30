package client.events;

import java.util.EventObject;
import network.User;

public class InvitedToPlayEvent extends EventObject {
    private User invitor;
    
    public InvitedToPlayEvent(Object source, User invitor) {
        super(source);
        this.invitor = invitor;
    }
    
    public User getInvitor() {
        return invitor;
    }
}