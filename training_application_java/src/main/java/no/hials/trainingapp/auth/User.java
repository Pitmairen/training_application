package no.hials.trainingapp.auth;

/**
 * Defines the interface which represents a user used for authentication
 *
 * @author Per Myren <progrper@gmail.com>
 */
public interface User
{

    /**
     * Returns true if the user is authenticated.
     *
     * @return true if authenticated
     */
    public boolean isAuthenticated();

    /**
     * Returns the name of the user
     *
     * @return the name
     */
    public String getName();

    /**
     * Returns the id of the user
     *
     * @return the user id
     */
    public int getId();

}
