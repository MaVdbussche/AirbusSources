package vandenbussche.airbussources.exceptions;


public class ExistingLoginException extends Exception {

    public ExistingLoginException(String message){
        super(message);
    }
}
