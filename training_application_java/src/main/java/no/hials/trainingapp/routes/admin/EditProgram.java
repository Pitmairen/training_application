package no.hials.trainingapp.routes.admin;

import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.sql.SQLException;
import no.hials.trainingapp.routing.FormInput;

/**
 * Edit a training program
 */
public class EditProgram extends FormRoute {

    public EditProgram(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws Exception {

        DataItem program = getProgram();

        if (program == null) {
            Spark.halt(404);
            return null;
        }

        FormInput form = getFormInput();

        form.addRequiredInputs("programName", "programDesc");

        if (getRequest().requestMethod().equals("POST")) {

            // Update the program so the data is available in the templates
            // even if there is a validation error.
            program.put("program_name", form.getValue("programName", ""));
            program.put("program_description", form.getValue("programDesc", ""));

            if (form.postedAndValid()) {

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    ds.updateProgram(program);
                    tx.commit();
                });

                getResponse().redirect("/admin/");
                flashMessage("The program has been updated");
                Spark.halt();
            }

        }

        setData("program", program);

        return renderTemplate("admin/edit-program");
    }

    private DataItem getProgram() throws SQLException {

        try {
            int id = Integer.parseInt(getRequest().params("prog_id"));
            return getDataSource().getProgramById(id);
        } catch (NumberFormatException e) {
            Spark.halt(404);
        }

        return null;
    }

}
