package client.battleship;

import client.battleship.events.bscomponents.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JPanel;

abstract class BrowseShipPanel extends JPanel {
    protected List<BrowseShipPanelActionListener> actionListeners = new LinkedList<>();
    protected List<BrowseShipPanelEmptyListener> emptyListeners = new LinkedList<>();
    
    protected BrowseShipPanel() {
        super();
    }

    public void addBrowseShipPanelActionListener(BrowseShipPanelActionListener listener) {
        actionListeners.add(listener);
    }
    
    public void addBrowseShipPanelEmptyListener(BrowseShipPanelEmptyListener listener) {
        emptyListeners.add(listener);
    }
    
    public abstract Ship getSelectedShip();
    public abstract void deleteSelectedShip();
    
    protected void listenAction(BrowseShipPanelActionEvent e) {
        ListIterator<BrowseShipPanelActionListener> iterator = actionListeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().actionPerformed(e);
        }
    }
    
    protected void listenEmpty(BrowseShipPanelEmptyEvent e) {
        ListIterator<BrowseShipPanelEmptyListener> iterator = emptyListeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().panelIsEmpty(e);
        }
    }
}
