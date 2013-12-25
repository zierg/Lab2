package client.battleship;

import client.battleship.events.BrowseShipPanelActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

abstract class BrowseShipPanel extends JPanel {
    protected List<BrowseShipPanelActionListener> listeners = new LinkedList<>();
    
    public BrowseShipPanel() {
        super();
    }

    public void addBrowseShipPanelActionListener(BrowseShipPanelActionListener listener) {
        listeners.add(listener);
    }
    
    public abstract Ship getSelectedShip();
    public abstract void deleteSelectedShip();
}
