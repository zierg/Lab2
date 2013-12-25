package client.battleship;

import client.battleship.events.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;

class JButtonBattlefield extends JPanelBattlefield {
    private class PlayerFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            JButtonField a = (JButtonField) ae.getSource();
            //a.setVisibleField(true);
            //a.setEnabled(false);
            listenAction(new BattlefieldActionEvent(this, ae.getActionCommand()));
        }
    }
    
    private boolean enabled;
    public static final int SIDE_FIELDS_COUNT = 10;
    public static final int TOTAL_FIELDS_COUNT = SIDE_FIELDS_COUNT*SIDE_FIELDS_COUNT;
    
    private JButtonField[] fields = new JButtonField[TOTAL_FIELDS_COUNT];
    
    public JButtonBattlefield(boolean enabled) {
        super();
        this.enabled = enabled;
        setLayout(new GridLayout(SIDE_FIELDS_COUNT,SIDE_FIELDS_COUNT));
        PlayerFieldListener listener = new PlayerFieldListener();
        for (int i = 0; i < TOTAL_FIELDS_COUNT; i++) {
            fields[i] = new BackgroundViewField(enabled, i);
            fields[i].addActionListener(listener);
            fields[i].setFill( ( (i%2 == 0 && (i/10)%2 != 0) || (i%2 != 0 && (i/10)%2 == 0) ) ? true : false );
            add(fields[i]);
        }
        
    }
        
    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        for (int i = 0; i < TOTAL_FIELDS_COUNT; i++) {
            fields[i].setEnabled(enabled);
        }
    }
    
    @Override
    public boolean attack(int index) {
        return fields[index].attack();
    }
    
    @Override
    public void setFill(int index, boolean filled) {
        fields[index].setFill(filled);
    }
    
    @Override
    public void setAllowTurn(boolean allow) {
        
    }
    
    @Override
    public void setFieldEnabled(int index, boolean enabled) {
        fields[index].setEnabled(enabled);
    }
    
    @Override
    public void setAvailableField(int index, boolean available) {
        fields[index].setAvailableField(available);
    }
    
    @Override
    public void setVisibleField(int index, boolean visible) {
        fields[index].setVisibleField(visible);
    }
    
    @Override
    public void addShip(int index, Ship ship) throws UncorrectFieldException {
        int increment = ship.getRotation() == Ship.SHIP_HORIZONTAL ? 1 : SIDE_FIELDS_COUNT;
        
        if (!isFieldForShipCorrect(index, ship)) {
            throw new UncorrectFieldException();
        }
        
        for (int i = 0; i < ship.getShipSize()*increment; i+=increment) {
            setFill(index + i, true);
            setVisibleField(index + i, true);
        }
    }
    
    private boolean isFieldForShipCorrect(int index, Ship ship) {
        int increment = ship.getRotation() == Ship.SHIP_HORIZONTAL ? 1 : SIDE_FIELDS_COUNT;
        int rotation = ship.getRotation();
        int size = ship.getShipSize();
        if (index + increment*(size-1) >= TOTAL_FIELDS_COUNT
                || (rotation == Ship.SHIP_HORIZONTAL 
                && index%SIDE_FIELDS_COUNT > ((index + increment*(size-1))%SIDE_FIELDS_COUNT)) ) { // Это очень плохо. Но для shipSize<=10 сгодится
            return false;
        }
        return true;
    }
    
    private void listenAction(BattlefieldActionEvent e) {
        ListIterator<BattlefieldActionListener> iterator = listeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().actionPerformed(e);
        }
    }
}
