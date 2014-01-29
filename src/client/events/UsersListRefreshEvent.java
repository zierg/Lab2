package client.events;

import java.util.Collection;
import java.util.EventObject;
import network.User;

public class UsersListRefreshEvent extends EventObject {
    private Collection<User> usersList;
    
    public UsersListRefreshEvent(Object source, Collection<User> usersList) {
        super(source);
        this.usersList = usersList;
    }
    
    public Collection<User> getUsersList() {
        return usersList;
    }
}