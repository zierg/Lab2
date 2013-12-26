package client.battleship;

import javax.swing.JPanel;
import java.util.List;
import java.util.LinkedList;
import client.battleship.events.*;

abstract class JPanelBattlefield extends JPanel {
   
    protected List<BattlefieldActionListener> listeners = new LinkedList<>();
    
    public JPanelBattlefield() {
        super();
    }

    public void addBattlefieldActionListener(BattlefieldActionListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public abstract void setEnabled(boolean enabled);
    
    public abstract void setAvailable(boolean available);
    public abstract boolean attack(int index);
    public abstract void setFieldEnabled(int index, boolean enabled);
    public abstract void setAvailableField(int index, boolean available);
    public abstract void setFill(int index, boolean filled);
    public abstract void setAllowTurn(boolean allow);
    public abstract void addShip(int index, Ship ship) throws UncorrectFieldException;
    public abstract void setVisibleField(int index, boolean visible);
}
