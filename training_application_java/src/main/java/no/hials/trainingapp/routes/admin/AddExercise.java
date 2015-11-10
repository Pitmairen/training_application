package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Add new exercises
 */
public class AddExercise extends FormRoute {

    public AddExercise(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        if (getRequest().requestMethod().equals("POST")) {

            validateFormInput();

            if (!hasValidationErrors()) {

                DataItem d = new DataItem();
                Request r = getRequest();

                d.put("exercise_name", r.queryParams("exerciseName"));
                d.put("exercise_description", r.queryParams("exerciseDesc"));

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    ds.storeNewExercise(d);
                    tx.commit();
                });

                getResponse().redirect("/admin/");
                flashMessage("The exercise has been added");
                Spark.halt();

            }

        }

        return renderTemplate("admin/add-exercise");
    }

    private void validateFormInput() {

        checkRequiredValues();

    }

    private void checkRequiredValues() {
        String[] requiredParams = new String[]{
            "exerciseDesc", "exerciseName"};

        Request req = getRequest();

        for (String param : requiredParams) {
            if (req.queryParams(param).isEmpty()) {
                addValidationError(param + " is required");
            }
        }
    }
}
