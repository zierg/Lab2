package client.battleship;

interface Ship {
    public static final int SHIP_HORIZONTAL = 1;
    public static final int SHIP_VERTICAL = 0;
    
    public void toggleRotation();
    public int getRotation();
    public void setRotation(int rotation);
}
