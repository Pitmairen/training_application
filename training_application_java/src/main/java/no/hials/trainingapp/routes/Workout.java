package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * A specific workout that have been selected in the GUI.
 *
 * @author Kristian Honningsvag.
 */
public class Workout extends TemplateRoute {

    public Workout(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        List<DataItem> workout = getDataSource().getWorkout(Integer.parseInt(getRequest().params("id")), Integer.parseInt(getRequest().params("id2")));

        setData("workout", workout);

        return renderTemplate("workout");
    }
}
