package server;

import java.io.IOException;
import network.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ServerThread extends Thread {
    private User user = null;
    private ServerThreadMessenger serverThreadMessenger;
    
    public ServerThread(Socket clientSocket) {
        try {
            NetworkMessenger messenger = new NetworkMessenger(clientSocket);
            serverThreadMessenger = new ServerThreadMessenger(messenger);
        } catch (IOException ex) {
            return;
        }
        start();
    }
    
    @Override
    public void run() {
        if ( !createUser() ) {
            System.out.println("Can not add user.");
            return;
        }                
        communicate();
    }
    
    private boolean createUser() {
        UserAndSocket userAndSocket = serverThreadMessenger.createUser();
        if (user == null) {
            return false;
        }
        user = userAndSocket.getUser();
        
        Server.addUser(user, userAndSocket.getSocket());
        return true;
    }
    
    private void communicate() {
        try {
            while(true) {
                serverThreadMessenger.waitMessage();
            }
        } catch (IOException ex) {
             System.out.println("User " + user + " is offline.");
             Server.removeUser(user);
        }
    }
}
