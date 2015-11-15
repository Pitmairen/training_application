package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import static spark.Spark.halt;

/**
 * A specific workout that have been selected in the GUI.
 *
 * @author Kristian Honningsvag.
 */
public class Workout extends FormRoute {

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

        // When user submits workout form.
        if (getRequest().requestMethod().equals("POST")) {

            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                for (DataItem set : sets) {

                    try {
                        Integer setID = set.getInteger("set_id");
                        set.put("set_reps_done", Integer.parseInt(getRequest().queryParams("set-" + set.getInteger("set_id") + "-RepsDone")));
                        set.put("set_weight_done", Integer.parseInt(getRequest().queryParams("set-" + set.getInteger("set_id") + "-LoadUsed")));
                    }
                    catch (NumberFormatException ex) {
                        System.err.println(ex);
                    }

                    // Store the completed set in the database.
                    getDataSource().storeSetDone(set);
                }
                // Mark the workout as done.
                Integer workoutID = workout.getInteger("workout_id");
                String userComment = getRequest().queryParams("userComment");
                getDataSource().storeExerciseDone(workoutID, userComment);

                tx.commit();

            });
            
            flashMessage("The workout has been completed");
            getResponse().redirect("/workoutLog/"+workout.getInteger("workout_id"));
            Spark.halt();
            
        }
        return renderTemplate("workout");
    }
}
