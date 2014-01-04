package server;

import network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import logger.LoggerManager;
import settings.ConfigReader;
import settings.PropertyConfigReader;

public class Server {    
    private static final String PROPERTIES_FILE = "properties.ini";
    
    private static Vector<User> usersList = new Vector<>();
    private static Map<User, ServerThread> usersMap = new HashMap<>();
    
    synchronized static boolean addUser(User user, ServerThread userThread) {
        if (userExists(user)) {
            return false;
        }
        usersList.add(user);
        usersMap.put(user, userThread);
        ServerLogger.trace("User " + user + " has been added to users list.");
        refreshUsersListAfterAdding(user);
        return true;
    }
    
    synchronized static void removeUser(User user) {
        usersList.remove(user);
        usersMap.remove(user);
        ServerLogger.trace("User " + user + " has been removed from users list.");
        refreshUsersListForAll();
    }
    
    synchronized static void setUserFree(User user, boolean free) {
        for (Map.Entry<User, ServerThread> entry : usersMap.entrySet()) {
            User currentUser = entry.getKey();
            if (currentUser.equals(user) && currentUser.isFree() != free) {
                currentUser.setFree(free);
                refreshUsersListForAll();
                return;
            }
        }
    }
    
    synchronized static Vector<User> getUsers() {
        Vector<User> newUL = new Vector<>();
        for (User currentUser:usersList) {
            newUL.add(currentUser.clone());
        }
        return newUL;
    }
    
    static ServerThread getUserServerThread(User user) {
        for (Map.Entry<User, ServerThread> entry : usersMap.entrySet()) {
            if (entry.getKey().equals(user)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        try {
            ConfigReader configReader = new PropertyConfigReader(PROPERTIES_FILE);
            int port = Integer.parseInt(configReader.readValue("PORT"));
            ServerSocket serverSocket = new ServerSocket(port);
            ServerLogger.trace("Server is running.");
            while (true) {
                Socket client = serverSocket.accept();  // заставляем сервер ждать подключений и выводим сообщение,
                ServerLogger.trace("Got a client.");    // когда кто-то связался с сервером
                new ServerThread(client);
            }
        } catch (IOException | NumberFormatException ex) {
            String message = Server.class.toString() + ex;
            LoggerManager.ERROR_CONSOLE_LOGGER.error(message);
            ServerLogger.error(message);
        }
    }
    
    private static boolean userExists(User user) {
        for (User currentUser:usersList) {
            if (currentUser.equals(user)) {
                return true;
            }
        }
        return false;
    }
    
    private static void refreshUsersListForAll() {
        for (Map.Entry<User, ServerThread> entry : usersMap.entrySet()) {
            entry.getValue().getServerThreadMessenger().getUsersListRequested();
        }
    }
    
    private static void refreshUsersListAfterAdding(User addedUser) {
        for (Map.Entry<User, ServerThread> entry : usersMap.entrySet()) {
            if (!entry.getKey().equals(addedUser)) {
                entry.getValue().getServerThreadMessenger().getUsersListRequested();
            }
        }
    }
    
    private Server() {}
}
