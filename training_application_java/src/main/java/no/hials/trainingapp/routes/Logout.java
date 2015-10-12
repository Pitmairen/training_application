package no.hials.trainingapp.routes;

import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

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
    public ModelAndView get() throws Exception
    {
        return renderTemplate("logout");
    }

    @Override
    public ModelAndView post() throws Exception
    {
        Auth.logoutUser(getRequest());
        getResponse().redirect("/login");
        halt();
        return null;
    }
}
