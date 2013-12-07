package client.battleship;

interface Field {
    public AttackTypes attack();
    public void setFill(boolean isFilled);
}
