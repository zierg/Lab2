package client;

import java.io.IOException;
import network.Message;
import network.Messenger;

public class NetworkClientMessenger extends ClientMessenger {
    
    private final Messenger messenger;
    
    public NetworkClientMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
    
    @Override
    public boolean login(String userName) {
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
