package network;

import java.io.Serializable;

public class Message implements Serializable {
    /**
     * Attributes of this type must be like this:
     * String errorText
     */
    public static final int ERROR = -1;
    
    
    public static final int TURN = 0;
    
    /**
     * Attributes of this type must be like this:
     * String userName
     */
    public static final int AUTHORIZATION = 1;
    
    private static final int[] TYPES = {
        TURN,
        AUTHORIZATION
    };
    
    private int type;
    private Serializable[] attributes;
    
    public Message(int type, Serializable ... attributes) throws UncorrectMessageTypeException {
        if (!isTypeCorrect(type)) {
            throw new UncorrectMessageTypeException();
        }
        this.type = type;
        this.attributes = attributes;
    }
    
    public int getType() {
        return type;
    }
    
    public Serializable[] getAttributes() {
        return attributes;
    }
    
    private boolean isTypeCorrect(int type) {
        for (int currentType:TYPES) {
            if (type == currentType) {
                return true;
            }
        }
        return false;
    }
}
