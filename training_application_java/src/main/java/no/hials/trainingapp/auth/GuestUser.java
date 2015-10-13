package no.hials.trainingapp.auth;

/**
 * Represents a guest user
 * 
 * @author Per Myren <progrper@gmail.com>
 */
public class GuestUser implements User
{

    /**
     * Returns true if the user is authenticated.
     *
     * This method will always return false because a guest user is
     * never authenticated.
     *
     * @return false
     */
    @Override
    public boolean isAuthenticated()
    {
        return false;
    }
    
    /**
     * Returns the name of the user
     * 
     * @return the name
     */
    @Override
    public String getName()
    {
        return "Guest";
    }
    
    /**
     * Returns the id of the user
     *
     * Will be -1 for the guest user.
     * 
     * @return the user id (-1 for guest user)
     */
    @Override
    public int getId()
    {
        return -1;
    }

}
