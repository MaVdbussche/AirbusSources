package vandenbussche.airbussources.exceptions;

public class InvalidFieldException extends Exception {

    private String location;

    public InvalidFieldException(String message, String location){
        super(message);
        this.location = location;
    }

    public String getLocation(){return this.location;}
}
