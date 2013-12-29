package network;

import java.net.Socket;

public class UserAndSocket {
    private final User user;
    private final Socket socket;
    
    public UserAndSocket(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }
    
    public User getUser() {
        return user;
    }
    
    public Socket getSocket() {
        return socket;
    }
}
