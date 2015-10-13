package no.hials.trainingapp.auth;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A filter that makes sure all users are authenticated
 *
 * This filter will check that the current user is authenticated, if not the
 * user will be redirected to the login page.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class AuthenticationFilter implements Filter
{

    @Override
    public void handle(Request request, Response response) throws Exception
    {

        // Don't require authenticaion for static files.
        if (request.pathInfo().startsWith("/static/")) {
            return;
        }

        if (!Auth.isAuthenticated(request) && !request.pathInfo().startsWith("/login")) {
            response.redirect("/login");
            Spark.halt();
        }
    }

}
