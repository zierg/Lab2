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

    void getUsersListRequested() {
        try {
            // Так надо о_О:
            Vector<User> usersList = (Vector<User>) Server.getUsers();
            // И так тоже надо
            Vector<User> newUL = new Vector<>();
            for (User currentUser:usersList) {
                newUL.add(currentUser.clone());
            }
            //-----------------
            messenger.sendMessage(new Message(Message.RETURN_USER_LIST, newUL));    
        } catch (IOException ex) {
            //Добавить логгирование
        }
    }
    
    private void letsPlayRequested(Message message) {
        Object[] attrs = message.getAttributes();
        User player = (User) attrs[0];
        User opponent = (User) attrs[1];

        NetworkMessenger opponentMessenger = Server.getUserServerThread(opponent).
                getServerThreadMessenger().getMessenger();
        try {
            opponentMessenger.sendMessage(message);
            Server.setUserFree(player, false);
            Server.setUserFree(opponent, false);
        } catch (IOException ex) {
            System.out.println("NOOOoo");
            // Отправить запросившему игроку ошибку
        }
        System.out.println(player + " wanna play with " + opponent);
    }
    
    private void letsPlayAnswered(Message message) {
        boolean accept = (boolean) message.getAttributes()[2];
        User invitor = (User) message.getAttributes()[0];
        User opponent = (User) message.getAttributes()[1];
        NetworkMessenger invitorMessenger = Server.getUserServerThread(invitor).
                getServerThreadMessenger().getMessenger();
        if (!accept) {
            Server.setUserFree(invitor, true);
            Server.setUserFree(opponent, true);
        }
        try {
            invitorMessenger.sendMessage(message);
        } catch (IOException ex) {
            System.out.println("NOOOoo");
            // Отправить запросившему игроку ошибку
        }
    }
    
    private void turnRecieved(Message message) {
        sendMessageToOpponent(message);
    }
    
    private void turnResultRecieved(Message message) {
        sendMessageToOpponent(message);
    }
    
    private void playerIsReadyRecieved(Message message) {
        sendMessageToOpponent(message);
    }
    
    private void gameOverRecieved(Message message) {
        sendMessageToOpponent(message);
    }
    
    private void userIsFreeRecieved(Message message) {
        User user = (User) message.getAttributes()[0];
        Server.setUserFree(user, true);
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
            case Message.LETS_PLAY_ANSWER: {
                letsPlayAnswered(message);
                break;
            }
            case Message.TURN: {
                turnRecieved(message);
                break;
            }
            case Message.TURN_RESULT: {
                turnResultRecieved(message);
                break;
            }
            case Message.PLAYER_IS_READY: {
                playerIsReadyRecieved(message);
                break;
            }
            case Message.GAME_OVER: {
                gameOverRecieved(message);
                break;
            }
            case Message.USER_IS_FREE: {
                userIsFreeRecieved(message);
                break;
            }
            default: {
                // отправить юзеру ошибку
            }
        }
    }
    
    /**
     * Tries to cast first message's attribute to User
     * and sends message to him.
     * @param message Message to send
     */
    private void sendMessageToOpponent(Message message) {
        Object[] attrs = message.getAttributes();
        User opponent = (User) attrs[0];
        NetworkMessenger opponentMessenger = Server.getUserServerThread(opponent).
                getServerThreadMessenger().getMessenger();
        try {
            opponentMessenger.sendMessage(message);
        } catch (IOException ex) {
            
        }
    }
}
