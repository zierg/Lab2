package server;

import java.io.IOException;
import java.util.Vector;
import network.*;

public class ServerThreadMessenger {
    
    private final NetworkMessenger messenger;
    
    public ServerThreadMessenger(NetworkMessenger messenger) {
        this.messenger = messenger;
    }
    
    public NetworkMessenger getMessenger() {
        return messenger;
    }
    
    public User createUser() {
        try {
            Message authMessage = messenger.getMessage();
            if (authMessage.getType() != Message.AUTHORIZATION || authMessage==null) {
                return null;
            }
            String userName = (String) authMessage.getAttributes()[0];
            // Добавить проверку имени на уникальность
            User user = new User(userName);
            return user;
        } catch (IOException ex) {
             return null;
        }
    }
    
    public void waitMessage() throws IOException {
        Message message = messenger.getMessage();
        if (message==null) {
            return;
        }
        callMessageEvent(message);
    }

    int x = 0;
    void getUsersListRequested() {
        try {
            // Так надо о_О:
            Vector<User> usersList = (Vector<User>) Server.getUsers().clone();
            //-----------------
            messenger.sendMessage(new Message(Message.RETURN_USER_LIST, usersList));    
        } catch (IOException ex) {
            //Добавить логгирование
        }
    }
    
    private void letsPlayRequested(Message message) {
        Object[] attrs = message.getAttributes();
        User user1 = (User) attrs[0];
        User opponent = (User) attrs[1];

        NetworkMessenger opponentMessenger = Server.getUserServerThread(opponent).
                getServerThreadMessenger().getMessenger();
        try {
            opponentMessenger.sendMessage(message);
        } catch (IOException ex) {
            System.out.println("NOOOoo");
            // Отправить запросившему игроку ошибку
        }
        System.out.println(user1 + " wanna play with " + opponent);
    }
    
    private void callMessageEvent(Message message) {
        switch(message.getType()) {
            case Message.GET_USER_LIST: {
                getUsersListRequested();
                break;
            }
            case Message.LETS_PLAY: {
                letsPlayRequested(message);
                break;
            }
            default: {
                // отправить юзеру ошибку
            }
        }
    }
}
