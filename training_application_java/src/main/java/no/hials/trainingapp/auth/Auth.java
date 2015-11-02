package no.hials.trainingapp.auth;

import spark.Request;

/**
 * Authentication helper functions
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Auth {

    // Keys used in the session
    public static final String IS_AUTH_KEY = "isAuth";
    public static final String USERNAME_KEY = "username";
    public static final String USER_ID_KEY = "userId";
    public static final String IS_ADMIN_KEY = "isAdmin";

    /**
     * Checks if the user associated with the request is authenticated
     *
     * @param req the request object
     *
     * @return true if user is authenticated
     */
    public static boolean isAuthenticated(Request req) {

        boolean isAuth = false;

        Boolean value = req.session().attribute(IS_AUTH_KEY);

        if (value != null && value) {
            isAuth = true;
        }

        return isAuth;
    }

    /**
     * Returns the user associated with the request
     *
     * The user can be a guest or a authenticated user
     *
     * @param req the request object
     *
     * @return the user associated with the request.
     */
    public static User getUser(Request req) {

        if (isAuthenticated(req)) {
            return new AuthenticatedUser(req.session());
        } else {
            return new GuestUser();
        }
    }

    /**
     * Logs in a user with the given id and username
     *
     * @param req the request object
     * @param id the user id
     * @param username the username of the user
     */
    public static void loginUser(Request req, int id, String username) {

        req.session().attribute(IS_AUTH_KEY, true);
        req.session().attribute(USERNAME_KEY, username);
        req.session().attribute(USER_ID_KEY, id);
    }

    /**
     * Logs out the user associated with the request
     *
     * @param req the request object
     */
    public static void logoutUser(Request req) {

        req.session().removeAttribute(IS_AUTH_KEY);
        req.session().removeAttribute(USERNAME_KEY);
        req.session().removeAttribute(USER_ID_KEY);
    }

    /**
     * Checks if the user associated with the request is the admin
     *
     * @param req the request object
     *
     * @return true if the user is admin
     */
    public static boolean isAdmin(Request req) {

        boolean isAdmin = false;

        Boolean value = req.session().attribute(IS_ADMIN_KEY);

        if (value != null && value) {
            isAdmin = true;
        }

        return isAdmin;
    }

    /**
     * Logs in the admin user user
     *
     * @param req the request object
     */
    public static void loginAdmin(Request req) {
        req.session().attribute(IS_ADMIN_KEY, true);
    }

    /**
     * Logs out the admin user user
     *
     * @param req the request object
     */
    public static void logoutAdmin(Request req) {
        req.session().removeAttribute(IS_ADMIN_KEY);
    }
}
