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
        DataItem workout = getDataSource().getWorkout(Integer.parseInt(getRequest().params("id")));
        setData("workout", workout);

        List<DataItem> sets = getDataSource().getSets(Integer.parseInt(getRequest().params("id")));
        setData("sets", sets);

        if (workout == null) {
            halt(404);
        }

//        // When user submits workout form.
//        if (getRequest().requestMethod().equals("POST")) {
//            for (DataItem set : sets) {
//                Integer setID = set.getInteger("set_id");
//                String repsDone = getRequest().queryParams("set-" + set.getInteger("set_id") + "-RepsDone");
//                String loadUsed = getRequest().queryParams("set-" + set.getInteger("set_id") + "-LoadUsed");
//                setDone(setID, repsDone, loadUsed);
//            }
//            Integer workoutID = workout.getInteger("workout_id");
//            exerciseDone(workoutID);
//        }

        return renderTemplate("workout");
    }
}
