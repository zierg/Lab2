package client.events;

public interface NetworkClientMessengerListener {
    public void usersListRefreshed(UsersListRefreshedEvent e);
    public void invitedToPlay(InvitedToPlayEvent e);
    public void answerToInvitationRecieved(AnswerToInvitationRecievedEvent e);
    public void turnMade(NetworkTurnMadeEvent e);
    public void turnResult(NetworkTurnResultEvent e);
}
