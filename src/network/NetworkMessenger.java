package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import server.Server;

public class NetworkMessenger {
    
    private final Socket socket;
    private final InputStream sin;
    private final OutputStream sout;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    
    public NetworkMessenger(Socket socket) throws IOException {
        this.socket = socket;
        sin = socket.getInputStream();
        sout = socket.getOutputStream();
        output = new ObjectOutputStream(sout);
        input = new ObjectInputStream(sin);
    }
    
    public NetworkMessenger(String serverName, int port) throws IOException {
        this( new Socket(InetAddress.getByName(serverName), port) );
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public Message getMessage() throws IOException {
        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException ex) {
            System.out.println("grgrgrgrgrr");
            return null;
        }
    }
    
    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
        output.flush();
    }
}
