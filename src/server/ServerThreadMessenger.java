package server;

import static network.Message.MessageTypes;
import java.io.IOException;
import network.*;

class ServerThreadMessenger {
    
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
            if (authMessage==null || authMessage.getType() != MessageTypes.AUTHORIZATION) {
                return null;
            }
            String userName = (String) authMessage.getAttributes()[0];
            User user = new User(userName);
            return user;
        } catch (IOException ex) {
            ServerLogger.error(ServerThreadMessenger.class.toString() + ex);
            return null;
        }
    }
    
    public void waitMessage() throws IOException {
        Message message = messenger.getMessage();
        if (message==null) {
            return;
        }
        ServerLogger.trace("Message recieved: " + message);
        callMessageEvent(message);
    }

    void getUsersListRequested() {
        try {
            messenger.sendMessage(new Message(MessageTypes.RETURN_USER_LIST, Server.getUsers()));    
        } catch (IOException ex) {
            ServerLogger.error(ServerThreadMessenger.class.toString() + ex);
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
            ServerLogger.error(ServerThreadMessenger.class.toString() + ex);
        }
        ServerLogger.trace(player + " wanna play with " + opponent);
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
            ServerLogger.error(ServerThreadMessenger.class.toString() + ex);
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
    
    private void textMessageRecieved(Message message) {
        sendMessageToOpponent(message);
    }
    
    private void userIsFreeRecieved(Message message) {
        User user = (User) message.getAttributes()[1];
        Server.setUserFree(user, true);
        sendMessageToOpponent(message);
    }
    
    private void callMessageEvent(Message message) {
        switch(message.getType()) {
            case GET_USER_LIST: {
                getUsersListRequested();
                break;
            }
            case LETS_PLAY: {
                letsPlayRequested(message);
                break;
            }
            case LETS_PLAY_ANSWER: {
                letsPlayAnswered(message);
                break;
            }
            case TURN: {
                turnRecieved(message);
                break;
            }
            case TURN_RESULT: {
                turnResultRecieved(message);
                break;
            }
            case PLAYER_IS_READY: {
                playerIsReadyRecieved(message);
                break;
            }
            case GAME_OVER: {
                gameOverRecieved(message);
                break;
            }
            case TEXT_MESSAGE: {
                textMessageRecieved(message);
                break;
            }
            case USER_IS_FREE: {
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
        try {
        NetworkMessenger opponentMessenger = Server.getUserServerThread(opponent).
                getServerThreadMessenger().getMessenger();
        opponentMessenger.sendMessage(message);
        } catch (NullPointerException | IOException ex) {
            ServerLogger.error(ServerThreadMessenger.class.toString() + ex);
        }
    }
}
