package client;

import java.io.IOException;
import java.util.Vector;
import network.*;

public class NetworkClientMessenger{
    
    private final NetworkMessenger messenger;
    
    public NetworkClientMessenger(NetworkMessenger messenger) {
        this.messenger = messenger;
    }
    
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

    public Vector<User> getUsersList() {
        try {
            messenger.sendMessage(new Message(Message.GET_USER_LIST));
            Message usersListMessage = messenger.getMessage();
            return (Vector<User>) usersListMessage.getAttributes()[0];
        } catch (IOException ex) {
            return null;
        }
    }
}
