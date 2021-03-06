package client.battleship;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import client.battleship.events.bscomponents.*;
import settings.SettingsHandler;

final class JButtonBrowseShipPanel extends BrowseShipPanel {
    private class ShipActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButtonShip shipItem = (JButtonShip) e.getSource();
            if ( shipItem.isSelected() ) {
                resetShips(shipItem);
                selectedShip = shipItem;
                listenAction(new BrowseShipPanelActionEvent(this));
            } else {
                shipItem.setSelected(true);
            }
        }
    }
    
    private final SettingsHandler translation;
    // ---- Translation -------
    private String rotateButtonText;
    // ------------------------
    
    private static final int BROWSE_SHIP_SIDE_SIZE = 30;
    private final static int DEFAULT_ROTATION = JButtonShip.SHIP_HORIZONTAL;

    private JButtonShip selectedShip;
    
    private List<JButtonShip> shipList = new ArrayList<>();
    private JPanel shipPanels[];
       
    public JButtonBrowseShipPanel(SettingsHandler translation, int ... ships) {
        super();
        this.translation = translation;
        loadTranslation();
        shipPanels = new JPanel[ships.length];
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
                         
        constraints.weightx = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JButton rotateButton = createRotateButton(rotateButtonText);
        layout.setConstraints(rotateButton, constraints);
        add(rotateButton);        
     
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        JScrollPane shipScrollPane = createShipScrollPane(ships);
        layout.setConstraints(shipScrollPane, constraints);
        add(shipScrollPane);
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
                listenEmpty(new BrowseShipPanelEmptyEvent(this));
            }
        }
    }
    
    private void loadTranslation() {
        rotateButtonText = loadSetting(translation, "rotate_button", "Rotate");
    }
    
    private String loadSetting(SettingsHandler handler, String setting, String defaultValue) {
        String value = handler.readValue(setting);
        return (value==null ? defaultValue : value);
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
      
    private JButton createRotateButton(String rotateButtonText) {
        JButton rotateButton = new JButton(rotateButtonText);
        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedShip != null) {
                    selectedShip.toggleRotation();
                }
            }
        });
        return rotateButton;
    }
    
    private JScrollPane createShipScrollPane(int ... ships) {
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

        int unitIncrement = 10;
        JScrollBar horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        JScrollBar verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        horizontalScrollBar.setUnitIncrement(unitIncrement);
        verticalScrollBar.setUnitIncrement(unitIncrement);
        
        
        JScrollPane shipScrollPane = new JScrollPane(shipPanel);
        shipScrollPane.setHorizontalScrollBar(horizontalScrollBar);
        shipScrollPane.setVerticalScrollBar(verticalScrollBar);
        return shipScrollPane;
    }
}
