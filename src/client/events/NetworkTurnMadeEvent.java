package client.events;

import java.util.EventObject;

public class NetworkTurnMadeEvent extends EventObject {
    private int fieldNum;
    
    public NetworkTurnMadeEvent(Object source, int fieldNum) {
        super(source);
        this.fieldNum = fieldNum;
    }
    
    public int getFieldNumber() {
        return fieldNum;
    }
}
