package network;

import java.io.Serializable;

public class Message implements Serializable {
    public static final int TURN = 0;
    
    private int type;
    
    public Message() {
        
    }
}
