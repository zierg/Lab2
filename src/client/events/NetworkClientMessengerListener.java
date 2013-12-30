package client.events;

public interface NetworkClientMessengerListener {
    public void usersListRefreshed(usersListRefreshedEvent e);
    public void invitedToPlay(invitedToPlayEvent e);
    public void answerToInvitationRecieved(answerToInvitationRecievedEvent e);
}
