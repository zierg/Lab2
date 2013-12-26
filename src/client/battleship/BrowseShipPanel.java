package client.battleship;

import client.battleship.events.BrowseShipPanelActionListener;
import client.battleship.events.BrowseShipPanelEmptyListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

abstract class BrowseShipPanel extends JPanel {
    protected List<BrowseShipPanelActionListener> actionListeners = new LinkedList<>();
    protected List<BrowseShipPanelEmptyListener> emptyListeners = new LinkedList<>();
    
    public BrowseShipPanel() {
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
}
