package client.battleship.events;

import client.chat.ChatActionEvent;

public interface BattleshipFrameListener {
    public void turnMade(TurnMadeEvent e);
    public void turnResult(TurnResultEvent e);
    public void playerIsReady(PlayerIsReadyEvent e);
    public void gameOver(BSFrameGameOverEvent e);
    public void chatActionPerformed(ChatActionEvent e);
}
