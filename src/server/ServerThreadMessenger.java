package server;

import java.io.IOException;
import network.*;

public class ServerThreadMessenger {
    
    private final NetworkMessenger messenger;
    
    public ServerThreadMessenger(NetworkMessenger messenger) {
        this.messenger = messenger;
    }
    
    public NetworkMessenger getMessenger() {
        return messenger;
    }
    
    public UserAndSocket createUser() {
        try {
            Message authMessage = messenger.getMessage();
            if (authMessage.getType() != Message.AUTHORIZATION || authMessage==null) {
                return null;
            }
            String userName = (String) authMessage.getAttributes()[0];
            // Добавить проверку имени на уникальность
            User user = new User(userName);
            return new UserAndSocket(user, messenger.getSocket());
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

    public void getUsersListRequested() {
        try {
            messenger.sendMessage(new Message(Message.RETURN_USER_LIST, Server.getUsers()));
        } catch (IOException ex) {
            //Добавить логгирование
        }
    }
    
    private void callMessageEvent(Message message) {
        switch(message.getType()) {
            case Message.GET_USER_LIST: {
                getUsersListRequested();
                break;
            }
            default: {
                // отправить юзеру ошибку
            }
        }
    }
}
