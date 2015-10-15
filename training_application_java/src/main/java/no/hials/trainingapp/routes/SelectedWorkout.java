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
 * A workout that have been selected in the GUI.
 *
 * @author Kristian Honningsvag.
 */
public class SelectedWorkout extends TemplateRoute {

    public SelectedWorkout(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        List<DataItem> workouts = getDataSource().getNextWorkoutsForCustomer(getCurrentUser().getId(), 10);

        setData("workouts", workouts);

        return renderTemplate("selected_workout");
    }
}
