package server;

import network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import logger.LoggerManager;
import settings.ConfigReader;
import settings.PropertyConfigReader;

public class Server {    
    private static final String PROPERTIES_FILE = "properties.ini";
    
    private static Map<User, ServerThread> usersMap = new HashMap<>();
    
    synchronized static boolean addUser(User user, ServerThread userThread) {
        if (userExists(user)) {
            return false;
        }
        usersMap.put(user, userThread);
        ServerLogger.trace("User " + user + " has been added to users list.");
        refreshUsersListAfterAdding(user);
        return true;
    }
    
    synchronized static void removeUser(User user) {
        usersMap.remove(user);
        ServerLogger.trace("User " + user + " has been removed from users list.");
        refreshUsersListForAll();
    }
    
    synchronized static void setUserFree(User user, boolean free) {
        for (User currentUser : usersMap.keySet()) {
            if (currentUser.equals(user) && currentUser.isFree() != free) {
                currentUser.setFree(free);
                refreshUsersListForAll();
                return;
            }
        }
    }
    
    synchronized static HashSet<User> getUsers() {
        HashSet<User> setToReturn = new HashSet<>();
        for (User currentUser : usersMap.keySet()) {
            setToReturn.add(new User(currentUser));
        }
        return setToReturn;
    }
    
    static ServerThread getUserServerThread(User user) {
        return usersMap.get(user);
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
        return usersMap.containsKey(user);
    }
    
    private static void refreshUsersListForAll() {
        HashSet users = getUsers();
        for (ServerThread currentThread : usersMap.values()) {
            currentThread.getServerThreadMessenger().sendUsersList(users);
        }
    }
    
    private static void refreshUsersListAfterAdding(User addedUser) {
        HashSet users = getUsers();
        for (Map.Entry<User, ServerThread> entry : usersMap.entrySet()) {
            if (!entry.getKey().equals(addedUser)) {
                entry.getValue().getServerThreadMessenger().sendUsersList(users);
            }
        }
    }
    
    private Server() {}
}
