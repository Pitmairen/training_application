package no.hials.trainingapp.auth;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class GuestUser implements User
{

    @Override
    public boolean isAuthenticated()
    {
        return false;
    }

    @Override
    public String getName()
    {
        return "Guest";
    }

    @Override
    public int getId()
    {
        return -1;
    }

}
