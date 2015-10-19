package no.hials.trainingapp.routes.admin;

import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 *
 */
public class AdminIndex extends TemplateRoute {

    public AdminIndex(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws Exception {
        return renderTemplate("admin/index");
    }

}
