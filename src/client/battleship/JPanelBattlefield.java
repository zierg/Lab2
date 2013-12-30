package client.battleship;

import client.battleship.events.bscomponents.*;
import javax.swing.JPanel;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

abstract class JPanelBattlefield extends JPanel {
   
    protected List<BattlefieldActionListener> actionListeners = new LinkedList<>();
    protected List<GameOverListener> gameOverListeners = new LinkedList<>();
    
    public JPanelBattlefield() {
        super();
    }

    public void addBattlefieldActionListener(BattlefieldActionListener listener) {
        actionListeners.add(listener);
    }
    
    public void addBattlefieldGameOverListener(GameOverListener listener) {
        gameOverListeners.add(listener);
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
    
    protected void listenAction(BattlefieldActionEvent e) {
        ListIterator<BattlefieldActionListener> iterator = actionListeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().actionPerformed(e);
        }
    }
    
    protected void listenGameOver(GameOverEvent e) {
        ListIterator<GameOverListener> iterator = gameOverListeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().gameOver(e);
        }
    }
}
