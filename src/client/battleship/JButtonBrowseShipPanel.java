package client.battleship;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

final class JButtonBrowseShipPanel extends BrowseShipPanel {
    private class ShipActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButtonShip shipItem = (JButtonShip) e.getSource();
            if ( shipItem.isSelected() ) {
                resetShips(shipItem);
                selectedShip = shipItem;
            } else {
                shipItem.setSelected(true);
            }
        }
    }
    
    private static final int BROWSE_SHIP_SIDE_SIZE = 30;
    private final static int DEFAULT_ROTATION = JButtonShip.SHIP_HORIZONTAL;

    private JButtonShip selectedShip;
    private JScrollPane shipScrollPane;
    
    private List<JButtonShip> shipList = new ArrayList<>();
    private JPanel shipPanels[];
        
    public JButtonBrowseShipPanel(int ... ships) {
        super();
        shipPanels = new JPanel[ships.length];
        setLayout(new GridLayout(2,1));
        createRotateButton();
        createShipPanel(ships);
    }
    
    @Override
    public Ship getSelectedShip() {
        return selectedShip;
    }
    
    @Override
    public void deleteSelectedShip() {
        if (selectedShip != null) {
            shipPanels[selectedShip.getShipSize()-1].remove(selectedShip);
            shipPanels[selectedShip.getShipSize()-1].repaint();
            shipList.remove(selectedShip);
            if (shipList.isEmpty()) {
                // do something
            }
        }
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
    
    private void createRotateButton() {
        final JButton rotateButton = new JButton("Rotate");
        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedShip != null) {
                    selectedShip.toggleRotation();
                }
            }
        });
        add (rotateButton);
    }
    
    private void createShipPanel(int ... ships) {
        GridBagLayout shipPanelLayout = new GridBagLayout();
        JPanel shipPanel = new JPanel(shipPanelLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        ShipActionListener listener = new ShipActionListener();
        FlowLayout layout = new FlowLayout();
        for (int i = 0; i < ships.length; i++) {
            shipPanels[i] = new JPanel(layout);
            for (int j = 0; j < ships[i]; j++) {
                JButtonShip newShip = new JButtonShip(i+1, BROWSE_SHIP_SIDE_SIZE);
                newShip.addActionListener(listener);
                shipList.add(newShip);
                shipPanels[i].add(newShip);
            }
            shipPanelLayout.setConstraints(shipPanels[i], gbc);
            shipPanel.add(shipPanels[i]);
        }

        shipScrollPane = new JScrollPane(shipPanel);
        add(shipScrollPane);
    }
}
