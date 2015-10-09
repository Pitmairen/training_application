package no.hials.trainingapp.auth;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public interface User
{

    public boolean isAuthenticated();

    public String getName();

    public int getId();

}
