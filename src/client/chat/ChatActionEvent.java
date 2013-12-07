package client.chat;

import java.util.EventObject;

public class ChatActionEvent extends EventObject {
    private String message;
    
    public ChatActionEvent(Object source) {
        super(source);
    }
    
    public ChatActionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
