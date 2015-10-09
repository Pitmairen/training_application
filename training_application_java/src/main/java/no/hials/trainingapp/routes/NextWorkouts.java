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
public class NextWorkouts extends TemplateRoute
{

    public NextWorkouts(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView get() throws Exception
    {

        List<DataItem> workouts = getDataSource()
                .getNextWorkoutsForCustomer(getCurrentUser().getId(), 10);

        setData("workouts", workouts);

        return renderTemplate("workout-schedule");
    }
}
