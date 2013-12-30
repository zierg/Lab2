package client.events;

import java.util.EventObject;
import java.util.Vector;
import network.User;

public class UsersListRefreshedEvent1 extends EventObject {
    private Vector<User> usersList;
    
    public UsersListRefreshedEvent1(Object source, Vector<User> usersList) {
        super(source);
        this.usersList = usersList;
    }
    
    public Vector<User> getUsersList() {
        return usersList;
    }
}