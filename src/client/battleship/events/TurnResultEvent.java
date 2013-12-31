package client.battleship.events;

import java.util.EventObject;

public class TurnResultEvent extends EventObject {
    private int fieldNum;
    private boolean hit;
    
    public TurnResultEvent(Object source, int fieldNum, boolean hit) {
        super(source);
        this.fieldNum = fieldNum;
        this.hit = hit;
    }
    
    public int getFieldNumber() {
        return fieldNum;
    }
    
    public boolean getHit() {
        return hit;
    }
}
