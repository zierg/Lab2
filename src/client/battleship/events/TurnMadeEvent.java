package client.battleship.events;

import java.util.EventObject;

public class TurnMadeEvent extends EventObject {
    private int fieldNum;
    
    public TurnMadeEvent(Object source, int fieldNum) {
        super(source);
        this.fieldNum = fieldNum;
    }
    
    public int getFieldNumber() {
        return fieldNum;
    }
}
