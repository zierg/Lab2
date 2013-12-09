package client.battleship;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BrowseShipPanel extends JPanel {
    public BrowseShipPanel() {
        super();
        setLayout(new FlowLayout());
        add (new Ship(1, 30));
        add (new Ship(1, 30));
        add (new Ship(1, 30));
        add (new Ship(1, 30));
        add (new Ship(2, 30));
        add (new Ship(2, 30));
        add (new Ship(2, 30));
        add (new Ship(3, 30));
        add (new Ship(3, 30));
        add (new Ship(4, 30));
        /*add(new JButton());
        
        
        add(new JButton());
        add(new JButton());
        add(new JButton());
        add(new JButton());*/
    }
}
