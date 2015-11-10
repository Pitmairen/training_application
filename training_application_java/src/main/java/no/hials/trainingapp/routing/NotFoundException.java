package no.hials.trainingapp.routing;

/**
 * Represents something that was not found
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg){
        super(msg);
    }

}
