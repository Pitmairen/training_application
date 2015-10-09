package no.hials.trainingapp.auth;

import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class AuthenticationFilter implements Filter
{

    @Override
    public void handle(Request request, Response response) throws Exception
    {

        if (request.pathInfo().startsWith("/static/")) {
            return;
        }

        if (!Auth.isAuthenticated(request) && !request.pathInfo().startsWith("/login")) {
            response.redirect("/login");
            halt();
        }
    }

}
