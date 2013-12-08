package client.battleship;

import javax.swing.JButton;

abstract class JButtonField extends JButton {
    public abstract boolean attack();
    public abstract void setFill(boolean filled);   
    public abstract void setAvailableField(boolean available);
    public abstract void setVisibleField(boolean visible);
}
