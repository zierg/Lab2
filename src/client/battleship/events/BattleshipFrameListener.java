package client.battleship.events;

public interface BattleshipFrameListener {
    public void turnMade(TurnMadeEvent e);
    public void turnResult(TurnResultEvent e);
    public void playerIsPrepared(PlayerIsPreparedEvent e);
    public void gameOver(BSFrameGameOverEvent e);
}
