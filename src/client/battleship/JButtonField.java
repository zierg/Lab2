package client.battleship;

import javax.swing.JButton;

public abstract class JButtonField extends JButton {
    public abstract boolean attack();
    public abstract void setFill(boolean filled);   
}
