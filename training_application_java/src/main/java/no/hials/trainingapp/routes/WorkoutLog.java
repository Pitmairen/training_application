package no.hials.trainingapp.routes;

import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class WorkoutLog extends TemplateRoute
{

    public WorkoutLog(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView get() throws Exception
    {

        List<DataItem> workouts = getDataSource()
                .getWorkoutLogForCustomer(getCurrentUser().getId(), 10);

        setData("workouts", workouts);

        return renderTemplate("workout-log");
    }
}
