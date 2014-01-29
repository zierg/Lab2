package network;

import java.io.Serializable;

public class User implements Serializable {
    private final String userName;
    private boolean free = true;
    
    public User(String userName) {
        this.userName = userName;
    }
    
    public User(User user) {
        this.userName = user.userName;
        this.free = user.free;
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if ( !(obj instanceof User) ) {
            return false;
        }
        final User other = (User) obj;
        if (!this.userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
}
