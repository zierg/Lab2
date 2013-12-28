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
            Message authMessage = (Message) input.readObject();
            if (authMessage.getType() != Message.AUTHORIZATION) {
                return false;
            }
            String userName = (String) authMessage.getAttributes()[0];

            user = new User(userName, clientSocket);
            Server.addUser(user);
            System.out.println(user);
            return true;
        } catch (IOException | ClassNotFoundException ex) {
             return false;
        }
    }
    
    private void communicate() {
        try {
            while(true) {
                Message message = (Message) input.readObject();
                System.out.println(message.getType());
            }
        } catch (IOException ex) {
             System.out.println("User " + user + " is offline.");
        } catch (ClassNotFoundException ex) {
            // What? Not found? Impossible!
        }
    }
}
