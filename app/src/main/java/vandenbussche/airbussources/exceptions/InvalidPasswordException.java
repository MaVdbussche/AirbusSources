package vandenbussche.airbussources.exceptions;

/**
 *
 */
public class InvalidPasswordException extends Exception {

    /**
     * Standard constructor for the exception
     * @param message the message to be shown by the exception
     */
    public InvalidPasswordException(String message){
        super(message);
    }
}
