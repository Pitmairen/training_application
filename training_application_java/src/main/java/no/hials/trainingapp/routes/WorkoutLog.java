package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A specific workout that have been selected in the GUI.
 *
 * @author Kristian Honningsvag.
 */
public class WorkoutLog extends TemplateRoute {

    public WorkoutLog(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        DataItem customer = getDataSource().getCustomerById(getCurrentUser().getId());
        
        DataItem workout = getDataSource().getWorkout(Integer.parseInt(getRequest().params("id")));
        
        // Abort if workout does not exist or is not part of the user's program.
        if(workout == null || customer.getInteger("customer_program_id") != workout.getInteger("workout_program_id")){
            Spark.halt(404);
        }
        
        setData("workout", workout);

        List<DataItem> sets = getDataSource().getSets(Integer.parseInt(getRequest().params("id")));
        setData("sets", sets);

        return renderTemplate("workoutLog");
    }
}
