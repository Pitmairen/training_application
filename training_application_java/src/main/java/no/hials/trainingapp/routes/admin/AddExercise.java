package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormInput;
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

        FormInput form = getFormInput();

        form.addRequiredInputs("exerciseDesc", "exerciseName");

        if (form.postedAndValid()) {

            DataItem d = new DataItem();

            d.put("exercise_name", form.getValue("exerciseName"));
            d.put("exercise_description", form.getValue("exerciseDesc"));

            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                ds.storeNewExercise(d);
                tx.commit();
            });

            getResponse().redirect("/admin/");
            flashMessage("The exercise has been added");
            Spark.halt();

        }

        return renderTemplate("admin/add-exercise");
    }

}
