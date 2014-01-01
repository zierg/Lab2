package client.events;

import java.util.EventObject;
import network.User;

public class InviteToPlayEvent extends EventObject {
    private User invitor;
    
    public InviteToPlayEvent(Object source, User invitor) {
        super(source);
        this.invitor = invitor;
    }
    
    public User getInvitor() {
        return invitor;
    }
}