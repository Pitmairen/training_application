package no.hials.trainingapp.routing;

/**
 * Exception used when an incoming request uses an unsupported HTTP method. 
 */
public class MethodNotSupportedException extends Exception
{

    private static final long serialVersionUID = 1L;

    public MethodNotSupportedException()
    {
        super("The http method is not supported");
    }
}
