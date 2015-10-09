package no.hials.trainingapp.auth;

import spark.Session;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class AuthenticatedUser implements User
{

    private final Session mSession;

    public AuthenticatedUser(Session session)
    {
        mSession = session;
    }

    @Override
    public boolean isAuthenticated()
    {
        return true;
    }

    @Override
    public String getName()
    {
        String value = mSession.attribute(Auth.USERNAME_KEY);
        return value == null ? "" : value;
    }

    @Override
    public int getId()
    {
        Integer value = mSession.attribute(Auth.USER_ID_KEY);
        return value == null ? -1 : value;
    }

}
