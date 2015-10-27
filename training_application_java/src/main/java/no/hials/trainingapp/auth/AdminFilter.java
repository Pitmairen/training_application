package no.hials.trainingapp.auth;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A filter that makes sure that only the admin can access the admin page
 *
 */
public class AdminFilter implements Filter {

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (!Auth.isAdmin(request)) {
            response.redirect("/login");
            Spark.halt();
        }
    }

}
