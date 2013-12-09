package client.battleship;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JPanel;

class BrowseShipPanel extends JPanel {
    private class ShipItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            Ship shipItem = (Ship) e.getItem();
            if ( shipItem.isSelected() ) {
                resetShips(shipItem);
                selectedShip = shipItem;
            }
        }  
    }
    
    private final static int DEFAULT_ROTATION = Ship.SHIP_HORIZONTAL;
    private final static int MAX_SHIPS = 4;
    private Ship selectedShip;
    
    private List<Ship> shipList = new LinkedList<>();
        
    public BrowseShipPanel() {
        super();
        FlowLayout layout = new FlowLayout();
        JButton b = new JButton("Rotate");
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedShip != null) {
                    selectedShip.toggleRotation();
                }
            }
        });
        add (b);
        
        
        setLayout(layout);
        ShipItemListener listener = new ShipItemListener();
        for (int i = 0; i < MAX_SHIPS; i++) {
            JPanel panel = new JPanel(layout);
            for (int j = i; j < MAX_SHIPS; j++) {
                Ship newShip = new Ship(i+1, 30);
                newShip.addItemListener(listener);
                shipList.add(newShip);
                panel.add(newShip);
            }
            add(panel);
        }
    }
    
    public Ship getSelectedShip() {
        return selectedShip;
    }
    
    private void resetShips(Ship dontReset) {
        ListIterator<Ship> iterator = shipList.listIterator();
        while ( iterator.hasNext() ) {
            Ship currentShip = iterator.next();
            if (currentShip != dontReset ) {
                if (currentShip.getRotation() != DEFAULT_ROTATION) {
                    currentShip.setRotation(DEFAULT_ROTATION);
                }
                if (currentShip.isSelected()) {
                    currentShip.setSelected(false);
                }
            }
        }
    }
}
