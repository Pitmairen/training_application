package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

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

        // (rewrite this by using only sparc)
        DataItem workout = getDataSource().getWorkout(Integer.parseInt(getRequest().params("id")), Integer.parseInt(getRequest().params("id2")));
        setData("workout", workout);

        List<DataItem> exercises = getDataSource().getExercises(1, 1, 1);
        setData("workout", exercises);

        if (workout == null) {
            halt(404);
        }

        return renderTemplate("workout");
    }
}
