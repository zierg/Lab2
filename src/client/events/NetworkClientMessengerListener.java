package client.events;

import client.chat.ChatActionEvent;

public interface NetworkClientMessengerListener {
    public void usersListRefreshed(UsersListRefreshEvent e);
    public void invitedToPlay(InviteToPlayEvent e);
    public void answerToInvitationRecieved(AnswerToInvitationEvent e);
    public void turnMade(NetworkTurnMadeEvent e);
    public void turnResult(NetworkTurnResultEvent e);
    public void playerIsReady(NetworkPlayerIsReadyEvent e);
    public void gameOver(NetworkGameOverEvent e);
    public void textMessageRecieved(ChatActionEvent e);
    public void errorRecieved(ErrorEvent e);
    public void userLeftGame(UserLeftGameEvent e);
}
