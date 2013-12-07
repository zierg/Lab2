package client.battleship;

import javax.swing.JButton;

abstract class JButtonField extends JButton {
    public abstract boolean attack();
    public abstract void setFill(boolean filled);   
}
