package client.battleship;

public class UncorrectFieldException extends Exception{
    public UncorrectFieldException() {
        super();
    }
    
    public UncorrectFieldException(String message) {
        super(message);
    }
    
    public UncorrectFieldException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UncorrectFieldException(Throwable cause) {
        super(cause);
    }
}
