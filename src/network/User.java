package network;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private final String userName;
    //private final Socket socket;
    private boolean free = true;
    
    public User(String userName, Socket socket) {
        this.userName = userName;
        //this.socket = socket;
    }
    
    public String getName() {
        return userName;
    }
    
    /*public Socket getSocket() {
        return socket;
    }*/
    
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
}
