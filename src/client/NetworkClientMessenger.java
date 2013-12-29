package client;

import client.events.*;
import network.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class NetworkClientMessenger{
    
    // MessageCatcher needs in this fields
    private boolean working = true;
    
    private class MessageCatcher extends Thread {
        
        public MessageCatcher() {
            start();
        }
        
        @Override
        public void run() {
            while (working) {
                try {
                    catchMessage();
                } catch (IOException ex) {
                    System.out.println("ohh");
                    return;               //!!!!
                }
                //System.out.println(working);
            }
        }
        
        private void catchMessage() throws IOException {
            Message message = messenger.getMessage();
            if (message==null) {
                return;
            }
            callMessageEvent(message);
        }
        
        private void callMessageEvent(Message message) {
            switch(message.getType()) {
                case Message.RETURN_USER_LIST: {
                    /*System.out.println(message.getAttributes());
                    Vector<User> us = (Vector<User>) message.getAttributes()[0];
                    System.out.println(us);
                    System.out.println("x = " + (String) message.getAttributes()[1]);
                    System.out.println("Test vector" + message.getAttributes()[2]);
                    /*for (User user : us) {
                        System.out.println(user);
                    }*/
                    listenUsersListRefreshed( (Vector<User>) message.getAttributes()[0] );
                    break;
                }
                case Message.LETS_PLAY: {
                    
                    break;
                }
                default: {
                    // отправить юзеру ошибку
                }
            }
        }
    }
    
    protected List<NetworkClientMessengerListener> listeners = new LinkedList<>();
    
    private final NetworkMessenger messenger;
    
    public NetworkClientMessenger(NetworkMessenger messenger) {
        this.messenger = messenger;
        new MessageCatcher();
    }
    
     public void addNetworkClientMessengerListener(NetworkClientMessengerListener listener) {
         listeners.add(listener);
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

    public void getUsersList() {
        try {
            messenger.sendMessage(new Message(Message.GET_USER_LIST));
        } catch (IOException ex) {
            return;
        }
        return;
    }
    
    public boolean letsPlay(User whoWantsPlay, User withWhomWantsPlay) {
        try {
            messenger.sendMessage(new Message(Message.LETS_PLAY, whoWantsPlay, withWhomWantsPlay));
        } catch (IOException ex) {
            return false;   // Лучше заменить на эксепшн
        }
        return false;
    }
    
    public void shutdown() {
        working = false;
    }
    
    private void listenUsersListRefreshed(Vector<User> usersList) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.usersListRefreshed(usersList);
        }
    }
}
