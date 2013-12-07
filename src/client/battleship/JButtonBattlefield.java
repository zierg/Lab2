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
            a.attack();
            listenAction(new BattlefieldActionEvent(this, ae.getActionCommand()));
        }
    }
    
    public static final int SIDE_FIELDS_COUNT = 10;
    private static final int TOTAL_FIELDS_COUNT = SIDE_FIELDS_COUNT*SIDE_FIELDS_COUNT;
    
    private JButtonField[] fields = new JButtonField[TOTAL_FIELDS_COUNT];
    
    public JButtonBattlefield(boolean enabled) {
        super();
        setLayout(new GridLayout(SIDE_FIELDS_COUNT,SIDE_FIELDS_COUNT));
        PlayerFieldListener listener = new PlayerFieldListener();
        for (int i = 0; i < TOTAL_FIELDS_COUNT; i++) {
            fields[i] = new BackgroundViewField(enabled, i);
            fields[i].addActionListener(listener);
            fields[i].setFill( (i%3 == 0) ? true : false );
            add(fields[i]);
        }
        
    }
    
    @Override
    public boolean attack(int index) {
        return false;
    }
    
    @Override
    public void setFill(int index, boolean filled) {
        
    }
    
    @Override
    public void setAllowTurn(boolean allow) {
        
    }
    
    private void listenAction(BattlefieldActionEvent e) {
        ListIterator<BattlefieldActionListener> iterator = listeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().actionPerformed(e);
        }
    }
}
