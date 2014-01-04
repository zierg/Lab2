package client.battleship;

import client.battleship.events.bscomponents.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class JButtonBattlefield extends JPanelBattlefield {
    private class PlayerFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            listenAction(new BattlefieldActionEvent(this, ae.getActionCommand()));
        }
    }
    
    private boolean enabled;
    public static final int SIDE_FIELDS_COUNT = 10;
    public static final int TOTAL_FIELDS_COUNT = SIDE_FIELDS_COUNT*SIDE_FIELDS_COUNT;
    
    private int totalShipFields = 0;
    
    private JButtonField[] fields = new JButtonField[TOTAL_FIELDS_COUNT];
    
    public JButtonBattlefield(boolean enabled) {
        super();
        this.enabled = enabled;
        setLayout(new GridLayout(SIDE_FIELDS_COUNT,SIDE_FIELDS_COUNT));
        PlayerFieldListener listener = new PlayerFieldListener();
        for (int i = 0; i < TOTAL_FIELDS_COUNT; i++) {
            fields[i] = new BackgroundViewField(enabled, i);
            fields[i].addActionListener(listener);
            fields[i].setFill(false);
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
            if (!enabled || (enabled && !fields[i].getVisibleField())) {
                fields[i].setEnabled(enabled);
            }
        }
    }
    
    @Override
    public boolean attack(int index) {
        boolean hit = fields[index].attack();
        if (hit) {
            totalShipFields--;
            if (totalShipFields==0) {
                listenGameOver(new GameOverEvent(this));
            }
        }
        return hit;
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
        if (!isFieldForShipCorrect(index, ship)) {
            throw new UncorrectFieldException();
        }
        
        blockFieldsBeforeShip(index, ship);
        putShip(index, ship);
        blockFieldsAfterShip(index, ship);
        totalShipFields+=ship.getShipSize();
    }
    
    @Override
    public void setAvailable(boolean available) {
        for (JButtonField field:fields) {
            field.setAvailableField(available);
        }
    }
    
    private void blockFieldsBeforeShip(int index, Ship ship) {
        switch (ship.getRotation()) {
            case Ship.SHIP_HORIZONTAL: {
                if (index % SIDE_FIELDS_COUNT > 0) {
                    setAvailableField(index-1, false);
                    if (index > SIDE_FIELDS_COUNT) {
                        setAvailableField(index-SIDE_FIELDS_COUNT-1, false);
                    }
                    if (index + SIDE_FIELDS_COUNT < TOTAL_FIELDS_COUNT) {
                        setAvailableField(index+SIDE_FIELDS_COUNT-1, false);
                    }
                }
                break;
            }
            case Ship.SHIP_VERTICAL: {
                if (index >= SIDE_FIELDS_COUNT) {
                    setAvailableField(index-SIDE_FIELDS_COUNT, false);
                    if (index % SIDE_FIELDS_COUNT > 0) {
                        setAvailableField(index-SIDE_FIELDS_COUNT-1, false);
                    }
                    if (index % SIDE_FIELDS_COUNT < SIDE_FIELDS_COUNT-1) {
                        setAvailableField(index-SIDE_FIELDS_COUNT+1, false);
                    }
                }
                break;
            }
        }
    }
    
    private void blockFieldsAfterShip(int index, Ship ship) {
        int rotation = ship.getRotation();
        int increment = (rotation == Ship.SHIP_HORIZONTAL) ? 1 : SIDE_FIELDS_COUNT;
        int endOfShip = index + (ship.getShipSize()-1)*increment;
        switch (rotation) {
            case Ship.SHIP_HORIZONTAL: {
                if (endOfShip % SIDE_FIELDS_COUNT < SIDE_FIELDS_COUNT-1) {
                    setAvailableField(endOfShip+1, false);
                    if (endOfShip > SIDE_FIELDS_COUNT-1) {
                        setAvailableField(endOfShip-SIDE_FIELDS_COUNT+1, false);
                    }
                    if (endOfShip + SIDE_FIELDS_COUNT < TOTAL_FIELDS_COUNT) {
                        setAvailableField(endOfShip+SIDE_FIELDS_COUNT+1, false);
                    }
                }
                break;
            }
            case Ship.SHIP_VERTICAL: {
                if (endOfShip + SIDE_FIELDS_COUNT < TOTAL_FIELDS_COUNT) {
                    setAvailableField(endOfShip+SIDE_FIELDS_COUNT, false);
                    if (endOfShip % SIDE_FIELDS_COUNT > 0) {
                        setAvailableField(endOfShip+SIDE_FIELDS_COUNT-1, false);
                    }
                    if (endOfShip % SIDE_FIELDS_COUNT < SIDE_FIELDS_COUNT-1) {
                        setAvailableField(endOfShip+SIDE_FIELDS_COUNT+1, false);
                    }
                }
                break;
            }
        }
    }
    
    private void putShip(int index, Ship ship) {
        int rotation = ship.getRotation();
        int increment = (rotation == Ship.SHIP_HORIZONTAL) ? 1 : SIDE_FIELDS_COUNT;
        int shift = (rotation == Ship.SHIP_HORIZONTAL) ? SIDE_FIELDS_COUNT : 1;
        for (int i = 0; i < ship.getShipSize()*increment; i+=increment) {
            try {
                if ( !(rotation == Ship.SHIP_VERTICAL && (index+i)%SIDE_FIELDS_COUNT==0) ) {
                    setAvailableField(index+i-shift, false);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {}
             try {                 
                if ( !(rotation == Ship.SHIP_VERTICAL && (index+i)%SIDE_FIELDS_COUNT==SIDE_FIELDS_COUNT-1) ) {
                    setAvailableField(index+i+shift, false);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {}
             
            setFill(index + i, true);
            setVisibleField(index + i, true);
        }
    }
    
    private boolean isFieldForShipCorrect(int index, Ship ship) {
        int rotation = ship.getRotation();
        int increment = (rotation == Ship.SHIP_HORIZONTAL) ? 1 : SIDE_FIELDS_COUNT;
        int size = ship.getShipSize();
        if (index + increment*(size-1) >= TOTAL_FIELDS_COUNT
                || (rotation == Ship.SHIP_HORIZONTAL 
                && index%SIDE_FIELDS_COUNT > ((index + increment*(size-1))%SIDE_FIELDS_COUNT)) ) { // Тут был бред
            return false;
        }
        for (int i = 0; i < size*increment; i+=increment) {
            if (!fields[index+i].getAvailableField() || fields[index+i].getFill()) {
                return false;
            }
        }
        return true;
    }
}
