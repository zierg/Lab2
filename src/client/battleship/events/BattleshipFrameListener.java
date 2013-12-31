package client.battleship.events;

public interface BattleshipFrameListener {
    public void turnMade(TurnMadeEvent e);
    public void turnResult(TurnResultEvent e);
    public void playerIsReady(PlayerIsReadyEvent e);
    public void gameOver(BSFrameGameOverEvent e);
}
