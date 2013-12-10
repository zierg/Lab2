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

class JButtonBrowseShipPanel extends BrowseShipPanel {
    private class ShipItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JButtonShip shipItem = (JButtonShip) e.getItem();
            if ( shipItem.isSelected() ) {
                resetShips(shipItem);
                selectedShip = shipItem;
            }
        }  
    }
    
    private final static int DEFAULT_ROTATION = JButtonShip.SHIP_HORIZONTAL;
    private final static int MAX_SHIPS = 4;
    private JButtonShip selectedShip;
    
    private List<JButtonShip> shipList = new LinkedList<>();
        
    public JButtonBrowseShipPanel(int sideSize) {
        super();
        FlowLayout layout = new FlowLayout();
        JButton rotateButton = new JButton("Rotate");
        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedShip != null) {
                    selectedShip.toggleRotation();
                }
            }
        });
        add (rotateButton);
        
        
        setLayout(layout);
        ShipItemListener listener = new ShipItemListener();
        for (int i = 0; i < MAX_SHIPS; i++) {
            JPanel panel = new JPanel(layout);
            for (int j = i; j < MAX_SHIPS; j++) {
                JButtonShip newShip = new JButtonShip(i+1, sideSize);
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
    
    private void resetShips(JButtonShip dontReset) {
        ListIterator<JButtonShip> iterator = shipList.listIterator();
        while ( iterator.hasNext() ) {
            JButtonShip currentShip = iterator.next();
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
