package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkMessenger implements Messenger {
    
    //private final Socket socket;
    private final InputStream sin;
    private final OutputStream sout;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    
    public NetworkMessenger(Socket socket) throws IOException {
        //this.socket = socket;
        sin = socket.getInputStream();
        sout = socket.getOutputStream();
        input = new ObjectInputStream(sin);
        output = new ObjectOutputStream(sout);
    }
    
    @Override
    public Message getMessage() throws IOException {
        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    @Override
    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
        output.flush();
    }
}