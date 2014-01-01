package client;

import client.chat.ChatActionEvent;
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
                    listenUsersListRefreshed( new UsersListRefreshEvent(this, (Vector<User>) message.getAttributes()[0]) );
                    break;
                }
                case Message.LETS_PLAY: {
                    listenInvitedToPlay(new InviteToPlayEvent(this, (User) message.getAttributes()[0]));
                    break;
                }
                case Message.LETS_PLAY_ANSWER: { 
                    listenAnswerToInvitationRecieved(
                            new AnswerToInvitationEvent(this,
                            (User) message.getAttributes()[1],
                            (boolean) message.getAttributes()[2])
                            );
                    break;
                }
                case Message.TURN: {
                    listenNetworkTurnMade(new NetworkTurnMadeEvent(this, (int) message.getAttributes()[1]));
                    break;
                }
                case Message.TURN_RESULT: {
                    listenNetworkTurnResult(new NetworkTurnResultEvent(this, 
                            (int) message.getAttributes()[1], 
                            (boolean) message.getAttributes()[2]));
                    break;
                }
                case Message.PLAYER_IS_READY: {
                    listenNetworkPlayerIsReady(new NetworkPlayerIsReadyEvent(this));
                    break;
                }
                case Message.GAME_OVER: {
                    listenNetworkGameOver(new NetworkGameOverEvent(this));
                    break;
                }
                case Message.TEXT_MESSAGE: {
                    listenTextMessage(new ChatActionEvent(this, (String) message.getAttributes()[1]));
                    break;
                }
                case Message.ERROR: {
                    listenError(new ErrorEvent(this, (String) message.getAttributes()[0]));
                    break;
                }
                case Message.USER_IS_FREE: {
                    listenUserLeftGame(new UserLeftGameEvent(this));
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
        
    }
    
    public void addNetworkClientMessengerListener(NetworkClientMessengerListener listener) {
        listeners.add(listener);
    }
    
    public boolean login(String userName) {
        try {
            Message authMessage = new Message(Message.AUTHORIZATION, userName);
            messenger.sendMessage(authMessage);
            Message answer = messenger.getMessage();    // Ждём ответ на авторизацию
            if (answer.getType() == Message.AUTHORIZATION) {
                new MessageCatcher();
                getUsersList();
                return true;
            } else {
                listenError(new ErrorEvent(this, (String) answer.getAttributes()[0]));
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
    }

    public void getUsersList() {
        try {
            messenger.sendMessage(new Message(Message.GET_USER_LIST));
        } catch (IOException ex) {
            return;
        }
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
    
    public void sendTurnResult(User opponent, int fieldNum, boolean hit) {
        try {
            messenger.sendMessage(new Message(Message.TURN_RESULT, opponent, fieldNum, hit));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void sendReadyMessage(User opponent) {
        try {
            messenger.sendMessage(new Message(Message.PLAYER_IS_READY, opponent));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void sendGameOverMessage(User opponent, User user) {
        try {
            messenger.sendMessage(new Message(Message.GAME_OVER, opponent, user));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void sendTextMessage(User opponent, String messageText) {
        try {
            messenger.sendMessage(new Message(Message.TEXT_MESSAGE, opponent, messageText));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void setUserFree(User opponent, User user) {
        try {
            messenger.sendMessage(new Message(Message.USER_IS_FREE, opponent, user));
        } catch (IOException ex) {
            // Лучше заменить на эксепшн
        }
    }
    
    public void shutdown() {
        working = false;
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
