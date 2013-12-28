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
    private Socket clientSocket = null;
    private InputStream sin;
    private OutputStream sout;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        start();
    }
    
    @Override
    public void run() {
        try {
            sin = clientSocket.getInputStream();
            sout = clientSocket.getOutputStream();
            output = new ObjectOutputStream(sout);
            input = new ObjectInputStream(sin);
            if ( !createUser() ) {
                System.out.println("Can not add user.");
                return;
            }                
        } catch (IOException ex) {
            System.out.println("some error occured");   // Добавить логгер
        }
        communicate();
    }
    
    private boolean createUser() {
        try {
            Message authMessage = getMessage();
            if (authMessage.getType() != Message.AUTHORIZATION) {
                return false;
            }
            String userName = (String) authMessage.getAttributes()[0];
            // Добавить проверку имени на уникальность
            user = new User(userName, clientSocket);
            Server.addUser(user);
            System.out.println(user);
            return true;
        } catch (IOException ex) {
             return false;
        }
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
