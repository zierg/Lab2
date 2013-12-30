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
                    
                    listenUsersListRefreshed( new UsersListRefreshedEvent(this, (Vector<User>) message.getAttributes()[0]) );
                    break;
                }
                case Message.LETS_PLAY: {
                    listenInvitedToPlay(new InvitedToPlayEvent(this, (User) message.getAttributes()[0]));
                    break;
                }
                case Message.LETS_PLAY_ANSWER: { 
                    listenAnswerToInvitationRecieved(
                            new AnswerToInvitationRecievedEvent(this,
                            (User) message.getAttributes()[1],
                            (boolean) message.getAttributes()[2])
                            );
                    break;
                }
                case Message.TURN: {
                    System.out.println("Hey! You are under attack!");
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
    
    public void letsPlay(User whoWantsPlay, User withWhomWantsPlay) {
        try {
            messenger.sendMessage(new Message(Message.LETS_PLAY, whoWantsPlay, withWhomWantsPlay));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void answerToInvitation(User whoWantsPlay, User withWhomWantsPlay, boolean accept) {
        try {
            messenger.sendMessage(new Message(Message.LETS_PLAY_ANSWER, whoWantsPlay, withWhomWantsPlay, accept));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void attack(User opponent, int fieldNum) {
        try {
            messenger.sendMessage(new Message(Message.TURN, opponent, fieldNum));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void shutdown() {
        working = false;
    }
    
    private void listenUsersListRefreshed(UsersListRefreshedEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.usersListRefreshed(e);
        }
    }
    
    private void listenInvitedToPlay(InvitedToPlayEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.invitedToPlay(e);
        }
    }
    
    private void listenAnswerToInvitationRecieved(AnswerToInvitationRecievedEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.answerToInvitationRecieved(e);
        }
    }
}
