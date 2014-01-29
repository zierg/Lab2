package client;

import static network.Message.MessageTypes;
import client.chat.ChatActionEvent;
import client.events.*;
import network.*;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import logger.LoggerManager;

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
                    if (working) {
                        listenError(new ErrorEvent(this, "Disconnected!"));
                        LoggerManager.ERROR_FILE_LOGGER.error("Disconnected " + ex);
                    }
                    return;
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
                case RETURN_USER_LIST: {
                    listenUsersListRefreshed( new UsersListRefreshEvent(this, (Collection<User>) message.getAttributes()[0]) );
                    break;
                }
                case LETS_PLAY: {
                    listenInvitedToPlay(new InviteToPlayEvent(this, (User) message.getAttributes()[0]));
                    break;
                }
                case LETS_PLAY_ANSWER: { 
                    listenAnswerToInvitationRecieved(
                            new AnswerToInvitationEvent(this,
                            (User) message.getAttributes()[1],
                            (boolean) message.getAttributes()[2])
                            );
                    break;
                }
                case TURN: {
                    listenNetworkTurnMade(new NetworkTurnMadeEvent(this, (int) message.getAttributes()[1]));
                    break;
                }
                case TURN_RESULT: {
                    listenNetworkTurnResult(new NetworkTurnResultEvent(this, 
                            (int) message.getAttributes()[1], 
                            (boolean) message.getAttributes()[2]));
                    break;
                }
                case PLAYER_IS_READY: {
                    listenNetworkPlayerIsReady(new NetworkPlayerIsReadyEvent(this));
                    break;
                }
                case GAME_OVER: {
                    listenNetworkGameOver(new NetworkGameOverEvent(this));
                    break;
                }
                case TEXT_MESSAGE: {
                    listenTextMessage(new ChatActionEvent(this, (String) message.getAttributes()[1]));
                    break;
                }
                case ERROR: {
                    listenError(new ErrorEvent(this, (String) message.getAttributes()[0]));
                    break;
                }
                case USER_IS_FREE: {
                    listenUserLeftGame(new UserLeftGameEvent(this));
                    break;
                } 
            }
        }
    }
    
    protected List<NetworkClientMessengerListener> listeners = new LinkedList<>();
    
    private final NetworkMessenger messenger;
    
    public NetworkClientMessenger(NetworkMessenger messenger) {
        this.messenger = messenger;
        
    }
    
    public void addNetworkClientMessengerListener(NetworkClientMessengerListener listener) {
        listeners.add(listener);
    }
    
    public boolean login(String userName) {
        try {
            Message authMessage = new Message(MessageTypes.AUTHORIZATION, userName);
            messenger.sendMessage(authMessage);
            Message answer = messenger.getMessage();
            if (answer.getType() == MessageTypes.AUTHORIZATION) {
                new MessageCatcher();
                getUsersList();
                return true;
            } else {
                listenError(new ErrorEvent(this, (String) answer.getAttributes()[0]));
                return false;
            }
        } catch (IOException ex) {
            LoggerManager.ERROR_FILE_LOGGER.error(NetworkClientMessenger.class.toString() + ex);
            return false;
        }
    }

    public void getUsersList() {
        sendMessage(new Message(MessageTypes.GET_USER_LIST));
    }
    
    public void letsPlay(User whoWantsPlay, User withWhomWantsPlay) {
        sendMessage(new Message(MessageTypes.LETS_PLAY, whoWantsPlay, withWhomWantsPlay));
    }
    
    public void answerToInvitation(User whoWantsPlay, User withWhomWantsPlay, boolean accept) {
        sendMessage(new Message(MessageTypes.LETS_PLAY_ANSWER, whoWantsPlay, withWhomWantsPlay, accept));
    }
    
    public void attack(User opponent, int fieldNum) {
        sendMessage(new Message(MessageTypes.TURN, opponent, fieldNum));
    }
    
    public void sendTurnResult(User opponent, int fieldNum, boolean hit) {
        sendMessage(new Message(MessageTypes.TURN_RESULT, opponent, fieldNum, hit));
    }
    
    public void sendReadyMessage(User opponent) {
        sendMessage(new Message(MessageTypes.PLAYER_IS_READY, opponent));
    }
    
    public void sendGameOverMessage(User opponent, User user) {
        sendMessage(new Message(MessageTypes.GAME_OVER, opponent, user));
    }
    
    public void sendTextMessage(User opponent, String messageText) {
        sendMessage(new Message(MessageTypes.TEXT_MESSAGE, opponent, messageText));
    }
    
    public void setUserFree(User opponent, User user) {
        sendMessage(new Message(MessageTypes.USER_IS_FREE, opponent, user));
    }
    
    public void shutdown() {
        working = false;
        messenger.shutdown();
    }
    
    private void sendMessage(Message message) {
        try {
            messenger.sendMessage(message);
        } catch (IOException ex) {
            LoggerManager.ERROR_FILE_LOGGER.error("Sending message error. " + 
                    NetworkClientMessenger.class.toString() + ex);
        }
    }
    
    private void listenUsersListRefreshed(UsersListRefreshEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.usersListRefreshed(e);
        }
    }
    
    private void listenInvitedToPlay(InviteToPlayEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.invitedToPlay(e);
        }
    }
    
    private void listenAnswerToInvitationRecieved(AnswerToInvitationEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.answerToInvitationRecieved(e);
        }
    }
    
    private void listenNetworkTurnMade(NetworkTurnMadeEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.turnMade(e);
        }
    }
    
    private void listenNetworkTurnResult(NetworkTurnResultEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.turnResult(e);
        }
    }
    
    private void listenNetworkPlayerIsReady(NetworkPlayerIsReadyEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.playerIsReady(e);
        }
    }
    
    private void listenNetworkGameOver(NetworkGameOverEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.gameOver(e);
        }
    }
    
    private void listenTextMessage(ChatActionEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.textMessageRecieved(e);
        }
    }
    
    private void listenError(ErrorEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.errorRecieved(e);
        }
    }
    
    private void listenUserLeftGame(UserLeftGameEvent e) {
        for (NetworkClientMessengerListener listener : listeners) {
            listener.userLeftGame(e);
        }
    }
}
