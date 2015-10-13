package no.hials.trainingapp.routes;

import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Log out the current user
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Logout extends TemplateRoute
{

    public Logout(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle()
    {
        if (getRequest().requestMethod().equals("POST")) {

            Auth.logoutUser(getRequest());
            getResponse().redirect("/login");
            Spark.halt();
        }

        return renderTemplate("logout");
    }

}
