package client.battleship;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JToggleButton;

class JButtonShip extends JToggleButton implements Ship {
    private static class ShipIcon implements Icon {
        private final int sideSize;
        private final int shipSize;
        private final Color color;

        private final int rotation;
        
        public ShipIcon(int shipSize, int sideSize, int rotation, Color color) {
            this.sideSize = sideSize;
            this.shipSize = shipSize;
            this.color = color;
            this.rotation = rotation;
        }

        @Override
        public int getIconHeight() {
            if (this.rotation == SHIP_HORIZONTAL) {
                return sideSize;
            } else {
                return (sideSize + BLOCK_INSET)*shipSize;
            }
        }

        @Override
        public int getIconWidth() {
            if (this.rotation == SHIP_HORIZONTAL) {
                return (sideSize + BLOCK_INSET)*shipSize;
            } else {
                return sideSize;
            }
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            if (this.rotation == SHIP_HORIZONTAL) {
                for (int i = 0; i < shipSize; i++) {
                    g.fillRect((sideSize*i) + BLOCK_INSET*i, 0, sideSize, sideSize);
                }
            } else {
                for (int i = 0; i < shipSize; i++) {
                    g.fillRect(0, (sideSize*i) + BLOCK_INSET*i, sideSize, sideSize);
                }
            }
        }
    }
    
    
    private static final int BLOCK_INSET = 1;
    
    private int rotation;
    
    private final ShipIcon horizontalUnselectedIcon;
    private final ShipIcon verticalUnselectedIcon;
    private final ShipIcon horizontalSelectedIcon;
    private final ShipIcon verticalSelectedIcon;
    
    private final int shipSize;
    private final int sideSize;
    
    public JButtonShip(int shipSize, int sideSize) {
        super();
        this.shipSize = shipSize;
        this.sideSize = sideSize;
        horizontalUnselectedIcon = new ShipIcon(shipSize, sideSize, SHIP_HORIZONTAL, Color.darkGray);
        verticalUnselectedIcon = new ShipIcon(shipSize, sideSize, SHIP_VERTICAL, Color.darkGray);
        horizontalSelectedIcon = new ShipIcon(shipSize, sideSize, SHIP_HORIZONTAL, Color.lightGray);
        verticalSelectedIcon = new ShipIcon(shipSize, sideSize, SHIP_VERTICAL, Color.lightGray);
        setRotation(SHIP_HORIZONTAL);
    }
    
    @Override
    public void toggleRotation() {
        if (rotation == SHIP_HORIZONTAL) {
            setRotation(SHIP_VERTICAL);
        } else {
            setRotation(SHIP_HORIZONTAL);
        }
    }
    
    @Override
    public int getRotation() {
        return rotation;
    }
    
    @Override
    public void setRotation(int rotation) {
        this.rotation = rotation;
        switch (rotation) {
            case SHIP_HORIZONTAL: {
                setPreferredSize(new Dimension(sideSize*shipSize + BLOCK_INSET*shipSize-BLOCK_INSET, sideSize));
                setIcon(horizontalUnselectedIcon);
                setSelectedIcon(horizontalSelectedIcon);
                break;
            }
            case SHIP_VERTICAL: {
                setPreferredSize(new Dimension(sideSize, sideSize*shipSize + BLOCK_INSET*shipSize-BLOCK_INSET));
                setIcon(verticalUnselectedIcon);
                setSelectedIcon(verticalSelectedIcon);
                break;
            }
            default: {
                throw new UncorrectModeException();
            }
        }
    }
}