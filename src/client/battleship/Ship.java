package client.battleship;

import java.awt.Dimension;
import javax.swing.JButton;

public class Ship extends JButton {
    private static final int BLOCK_INSET = 2;
    
    public Ship(int shipSize, int sideSize) {
        super();
        setPreferredSize(new Dimension(sideSize, sideSize*shipSize));
    }
}
