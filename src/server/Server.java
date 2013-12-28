package server;

import network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Server {
    public static final int PORT = 12345;
    
    private static Vector<User> usersList = new Vector<>();
    
    public synchronized static void addUser(User user) {
        usersList.add(user);
    }
    
    public synchronized static void removeUser(User user) {
        usersList.remove(user);
    }
    
    public synchronized static Vector<User> getUsers() {
        return usersList;
    }
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running.");
            while (true) {
                Socket client = serverSocket.accept();  // заставляем сервер ждать подключений и выводим сообщение,
                System.out.println("Got a client.");    // когда кто-то связался с сервером
                //User newUser = new User("Pit", client);
                //addUser(newUser);
                new ServerThread(client);
            }
        } catch (IOException ex) {
            
        }
    }
}
