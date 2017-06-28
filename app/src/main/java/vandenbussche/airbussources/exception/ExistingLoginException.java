package vandenbussche.airbussources.exception;


public class ExistingLoginException extends Exception {

    public ExistingLoginException(String message){
        super(message);
    }
}
