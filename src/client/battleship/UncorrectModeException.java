package client.battleship;

public class UncorrectModeException extends RuntimeException {
    public UncorrectModeException() {
        super();
    }
    
    public UncorrectModeException(String message) {
        super(message);
    }
    
    public UncorrectModeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncorrectModeException(Throwable cause) {
        super(cause);
    }
}
