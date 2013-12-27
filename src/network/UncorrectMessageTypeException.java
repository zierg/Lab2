package network;

public class UncorrectMessageTypeException extends RuntimeException {
    public UncorrectMessageTypeException() {
        super();
    }
    
    public UncorrectMessageTypeException(String message) {
        super(message);
    }
    
    public UncorrectMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncorrectMessageTypeException(Throwable cause) {
        super(cause);
    }
}