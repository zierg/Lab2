package network;

import java.io.Serializable;

public class Message implements Serializable {  
    public enum MessageTypes {
        /**
         * Attributes of this type must be like this:
         * String errorText
         */
        ERROR,
        
        /**
         * Attributes of this type must be like this:
         * String userName
         */
        AUTHORIZATION,
        
        /**
         * This type of message has no attributes
         */
        GET_USER_LIST,
        
        /**
         * Attributes of this type must be like this:
         * Vector<User> usersList
         */
        RETURN_USER_LIST,
        
        /**
         * Attributes of this type must be like this:
         * User whoWantsPlay, User withWhomWantsPlay
         */
        LETS_PLAY,
        
        /**
         * Attributes of this type must be like this:
         * User whoWantsPlay, User withWhomWantsPlay, boolean accept
         */
        LETS_PLAY_ANSWER,
        
        /**
         * Attributes of this type must be like this:
         * User opponent, int fieldNumber
         */
        TURN,
        
        /**
         * Attributes of this type must be like this:
         * User opponent, int fieldNumber, boolean hit
         */
        TURN_RESULT,
        
        /**
         * Attributes of this type must be like this:
         * User opponent
         */
        PLAYER_IS_READY,
        
        /**
         * Attributes of this type must be like this:
         * User opponent, User player
         */
        GAME_OVER,
        
        /**
         * Attributes of this type must be like this:
         * User opponent, String messageText
         */
        TEXT_MESSAGE,
        
        /**
         * Attributes of this type must be like this:
         * User opponent, User user
         */
        USER_IS_FREE
    }
   
    private MessageTypes type;
    private Serializable[] attributes;
    
    public Message(MessageTypes type, Serializable ... attributes) {
        this.type = type;
        this.attributes = attributes;
    }
    
    public MessageTypes getType() {
        return type;
    }
    
    public Serializable[] getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        StringBuilder stringMessage = new StringBuilder("Type = " + type);
        for (Serializable currentAttr : attributes) {
            stringMessage.append(", ");
            stringMessage.append(currentAttr.toString());
        }
        return stringMessage.toString();
    }
}
