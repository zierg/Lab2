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
            
            
            Message authMessage = (Message) input.readObject();
            String userName = (String) authMessage.getAttributes()[0];
            
            user = new User(userName, clientSocket);
            Server.addUser(user);
            System.out.println(user.getName());
        } catch (IOException ex) {
            try {
                System.out.println("User " + user + " is offline.");
                Server.removeUser(user);
            } catch (NullPointerException ex1) {System.out.println("asd");}
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean createUser() {
        
    }
}
