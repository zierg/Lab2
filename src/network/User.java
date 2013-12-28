package network;

import java.net.Socket;

public class User {
    private final String userName;
    private final Socket socket;
    
    public User(String userName, Socket socket) {
        this.userName = userName;
        this.socket = socket;
    }
    
    public String getName() {
        return userName;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public String toString() {
        return userName;
    }
}
