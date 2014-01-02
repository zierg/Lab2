package server;

import java.io.IOException;
import network.*;
import java.net.Socket;
import logger.LoggerManager;

class ServerThread extends Thread {
    
    private User user = null;
    private ServerThreadMessenger serverThreadMessenger;
    
    public ServerThread(Socket clientSocket) {
        try {
            NetworkMessenger messenger = new NetworkMessenger(clientSocket);
            serverThreadMessenger = new ServerThreadMessenger(messenger);
        } catch (IOException ex) {
            ServerLogger.error(ServerThread.class.toString() + ex);
            return;
        }
        start();
    }
    
    @Override
    public void run() {
        NetworkMessenger userMessenger = serverThreadMessenger.getMessenger();
        try {
            if ( !createUser() ) {
                ServerLogger.trace("Can not add user " + user);                
                userMessenger.sendMessage(new Message(Message.ERROR, "User already exists."));
                return;
            }
            else {
                userMessenger.sendMessage(new Message(Message.AUTHORIZATION, user.getName()));
            }
        } catch (IOException ex) {
            ServerLogger.error(ServerThread.class.toString() + ex);
            return;
        }
        communicate();
    }
    
    private boolean createUser() {
        user = serverThreadMessenger.createUser();
        if (user == null) {
            return false;
        }
        return Server.addUser(user, this);
    }
    
    public ServerThreadMessenger getServerThreadMessenger() {
        return serverThreadMessenger;
    }
    
    private void communicate() {
        try {
            while(true) {
                serverThreadMessenger.waitMessage();
            }
        } catch (IOException ex) {
             ServerLogger.trace("User " + user + " is offline.");
             ServerLogger.error(ServerThread.class.toString() + ex);
             Server.removeUser(user);
        }
    }
}
