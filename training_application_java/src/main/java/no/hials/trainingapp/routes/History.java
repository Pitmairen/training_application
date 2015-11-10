package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Pagination;
import no.hials.trainingapp.routing.Helpers;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Handles the workout log route
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class History extends TemplateRoute {

    public History(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        List<DataItem> exercises = getDataSource().getAllExercises();

        Pagination pag = new Pagination(20, Helpers.getPaginationPage(getRequest()));

        List<DataItem> workouts = getDataSource().getWorkoutLogForCustomer(getCurrentUser().getId(), pag);

        setData("pagination", pag);
        setData("workouts", workouts);
        setData("exercises", exercises);

        return renderTemplate("history");
    }
}
