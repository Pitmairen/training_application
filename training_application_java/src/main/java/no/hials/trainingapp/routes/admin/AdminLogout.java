package no.hials.trainingapp.routes.admin;

import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Logs out the current admin user
 */
public class AdminLogout extends TemplateRoute {

    public AdminLogout(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() {
        if (getRequest().requestMethod().equals("POST")) {

            Auth.logoutAdmin(getRequest());
            getResponse().redirect("/");
            Spark.halt();
        }

        return renderTemplate("admin/logout");
    }

}
