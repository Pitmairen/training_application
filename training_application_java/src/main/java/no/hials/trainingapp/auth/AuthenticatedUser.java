package no.hials.trainingapp.auth;

import spark.Session;

/**
 * Represents an authenticated user
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class AuthenticatedUser implements User
{

    private final Session mSession;

    /**
     * Creates a new authenticated user
     *
     * @param session the session associated with the user
     */
    public AuthenticatedUser(Session session)
    {
        mSession = session;
    }

    /**
     * Returns true if the user is authenticated.
     *
     * This method will always return true because an authenticated user is
     * always authenticated.
     *
     * @return true
     */
    @Override
    public boolean isAuthenticated()
    {
        return true;
    }

    /**
     * Returns the name of the user
     *
     * @return the name
     */
    @Override
    public String getName()
    {
        String value = mSession.attribute(Auth.USERNAME_KEY);
        return value == null ? "" : value;
    }

    /**
     * Returns the id of the user
     *
     * @return the user id
     */
    @Override
    public int getId()
    {
        Integer value = mSession.attribute(Auth.USER_ID_KEY);
        return value == null ? -1 : value;
    }

}
