package server;

import network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Server {
    public static final int PORT = 12345;
    
    private static Vector<User> usersList = new Vector<>();
    private static Map<User, ServerThread> usersMap = new HashMap<>();
    
    public synchronized static void addUser(User user, ServerThread userThread) {
        usersList.add(user);
        usersMap.put(user, userThread);
        System.out.println("User " + user + " has been added to users list.");
    }
    
    public synchronized static void removeUser(User user) {
        usersList.remove(user);
        usersMap.remove(user);
        System.out.println("User " + user + " has been removed from users list.");
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
                new ServerThread(client);
            }
        } catch (IOException ex) {
            
        }
    }
}
