package no.hials.trainingapp.auth;

import spark.Request;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Auth
{

    public static final String IS_AUTH_KEY = "isAuth";
    public static final String USERNAME_KEY = "username";
    public static final String USER_ID_KEY = "userId";

    public static boolean isAuthenticated(Request req)
    {

        boolean isAuth = false;

        Boolean value = req.session().attribute(IS_AUTH_KEY);

        if (value != null && value) {
            isAuth = true;
        }

        return isAuth;
    }

    public static User getUser(Request req)
    {

        if (isAuthenticated(req)) {
            return new AuthenticatedUser(req.session());
        }
        else {
            return new GuestUser();
        }
    }

    public static void loginUser(Request req, int id, String username)
    {

        req.session().attribute(IS_AUTH_KEY, true);
        req.session().attribute(USERNAME_KEY, username);
        req.session().attribute(USER_ID_KEY, id);
    }

    public static void logoutUser(Request req)
    {

        req.session().removeAttribute(IS_AUTH_KEY);
        req.session().removeAttribute(USERNAME_KEY);
        req.session().removeAttribute(USER_ID_KEY);
    }

}
