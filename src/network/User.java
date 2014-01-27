package network;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, Cloneable {
    private final String userName;
    private boolean free = true;
    
    public User(String userName) {
        this.userName = userName;
    }
    
    public String getName() {
        return userName;
    }
    
    public void setFree(boolean free) {
        this.free = free;
    }
    
    public boolean isFree() {
        return free;
    }
    
    @Override
    public String toString() {
        return userName;
    }
    
    @Override
    public User clone() {
        User clonedUser = new User(userName);
        clonedUser.free = this.free;
        return clonedUser;
    }
    
    public boolean equals(User user) {
        return userName.equals(user.userName);
    }
    
    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        return true;
    }
}
