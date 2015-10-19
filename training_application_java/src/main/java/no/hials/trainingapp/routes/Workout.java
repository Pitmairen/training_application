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

        DataItem workout = getDataSource().getWorkout(Integer.parseInt(getRequest().params("id")), Integer.parseInt(getRequest().params("id2")));
        setData("workout", workout);

//        List<DataItem> exercise = getDataSource().getExercises(Integer.parseInt(getRequest().params("id")), Integer.parseInt(getRequest().params("id2")));
//        setData("workout", exercise);
//
//        List<DataItem> set = getDataSource().getSets(Integer.parseInt(getRequest().params("id")), Integer.parseInt(getRequest().params("id2")));
//        setData("workout", set);

        if (workout == null) {
            halt(404);
        }

        return renderTemplate("workout");
    }
}
