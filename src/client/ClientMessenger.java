package client;

import java.io.IOException;
import network.Message;
import network.Messenger;

public class ClientMessenger {
    
    private final Messenger messenger;
    
    public ClientMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
    
    public boolean connect(String userName) {
        try {
            Message authMessage = new Message(Message.AUTHORIZATION, userName);
            messenger.sendMessage(authMessage);
        } catch (IOException ex) {
            //System.out.println("FAIL (nothing epic..)");
            return false;
        }
        //System.out.println("Successfully conected =)");
        return true;
    }
}
