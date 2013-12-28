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
        User user = serverThreadMessenger.createUser();
        if (user == null) {
            return false;
        }
        Server.addUser(user);
        return true;
    }
    
    private void communicate() {
        try {
            while(true) {
                Message message = getMessage();
                if (message == null) {
                    continue;
                }
                switch (message.getType()) {
                    case Message.GET_USER_LIST: {
                        sendMessage(new Message(Message.RETURN_USER_LIST, Server.getUsers()));
                        break;
                    }
                    default: {
                        // отправить юзеру ошибку
                        continue;
                    }
                }
                System.out.println(message.getType());
            }
        } catch (IOException ex) {
             System.out.println("User " + user + " is offline.");
             Server.removeUser(user);
        }
    }
    
    // Сделать получение сообщений в виде событий
    private Message getMessage() throws IOException {
        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    private void sendMessage(Message message) throws IOException {
        output.writeObject(message);
        output.flush();
    }
}
