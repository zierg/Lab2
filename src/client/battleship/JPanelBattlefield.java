package client.battleship;

import javax.swing.JPanel;
import java.util.List;
import java.util.LinkedList;
import client.battleship.events.*;

abstract class JPanelBattlefield extends JPanel {
    public static final int CREATE_MODE = 0;
    public static final int PLAY_MODE = 1;
    
    protected List<BattlefieldActionListener> listeners = new LinkedList<>();
    
    private int mode;
    
    public JPanelBattlefield() {
        super();
    }
    
    public JPanelBattlefield(int mode) {
        super();
        setMode(mode);
    }

    public void addBattlefieldActionListener(BattlefieldActionListener listener) {
        listeners.add(listener);
    }
    
    public final int getMode() {
        return mode;
    }
    
    public final void setMode(int mode) {
        switch (mode) {
            case CREATE_MODE:
            case PLAY_MODE: {
                this.mode = mode;
                break;
            }
            default: {
                throw new UncorrectModeException("Mode " + mode + " is uncorrect");
            }
        }
    }
    
    public abstract boolean attack(int index);
    public abstract void setFill(int index, boolean filled);
    public abstract void setAllowTurn(boolean allow);
}
