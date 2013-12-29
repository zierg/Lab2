package client.events;

import java.util.Vector;
import network.User;

public interface NetworkClientMessengerListener {
    public void usersListRefreshed(Vector<User> usersList);
}
