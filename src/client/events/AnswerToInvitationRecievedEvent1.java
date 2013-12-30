package client.events;

import java.util.EventObject;
import network.User;

public class AnswerToInvitationRecievedEvent1 extends EventObject {
    private User invitor;
    private boolean accept;
    
    public AnswerToInvitationRecievedEvent1(Object source, User invitor, boolean accept) {
        super(source);
        this.invitor = invitor;
        this.accept = accept;
    }
    
    public User getInvitor() {
        return invitor;
    }
    
    public boolean getAccept() {
        return accept;
    }
}