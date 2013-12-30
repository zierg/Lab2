package client.events;

import java.util.EventObject;
import network.User;

public class InvitedToPlayEvent1 extends EventObject {
    private User invitor;
    
    public InvitedToPlayEvent1(Object source, User invitor) {
        super(source);
        this.invitor = invitor;
    }
    
    public User getInvitor() {
        return invitor;
    }
}