package client.battleship;

import javax.swing.JButton;

abstract class JButtonField extends JButton {
    public abstract boolean attack();
    public abstract void setFill(boolean filled);
    public abstract boolean getFill();
    public abstract void setAvailableField(boolean available);
    public abstract boolean getAvailableField();
    public abstract void setVisibleField(boolean visible);
    public abstract boolean getVisibleField();
}
