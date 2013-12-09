package client.battleship;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JToggleButton;

class Ship extends JToggleButton {
    private static final int BLOCK_INSET = 1;
    
    private static class ShipIcon implements Icon {
        private final int shipSize;
        private final int sideSize;

        private final int inset;

        public ShipIcon(int shipSize, int sideSize, int inset) {
            this.sideSize = sideSize;
            this.shipSize = (sideSize + inset)*shipSize;
            this.inset = inset;
        }

        @Override
        public int getIconHeight() {
            return sideSize;
        }

        @Override
        public int getIconWidth() {
            return shipSize;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            for (int i = 0; i < shipSize; i++) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect((sideSize*i) + inset*i, 0, sideSize, sideSize);
            }
        }
    }
    
    public Ship(int shipSize, int sideSize) {
        super(new ShipIcon(shipSize, sideSize, BLOCK_INSET));
        setPreferredSize(new Dimension(sideSize*shipSize + BLOCK_INSET*shipSize-BLOCK_INSET, sideSize));
    }  
}