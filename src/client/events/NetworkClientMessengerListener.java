package client.events;

public interface NetworkClientMessengerListener {
    public void usersListRefreshed(UsersListRefreshedEvent1 e);
    public void invitedToPlay(InvitedToPlayEvent1 e);
    public void answerToInvitationRecieved(AnswerToInvitationRecievedEvent1 e);
}
