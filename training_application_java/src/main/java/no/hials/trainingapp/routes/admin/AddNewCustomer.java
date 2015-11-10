package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import no.hials.trainingapp.auth.Security;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Creates a new customer
 */
public class AddNewCustomer extends FormRoute {

    public AddNewCustomer(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        if (getRequest().requestMethod().equals("POST")) {

            validateFormInput();

            if (!hasValidationErrors()) {
                
                final DataItem customer = createCustomerForDB();
                final DataItem program = createProgramForCustomer(
                        customer.getString("customer_name"));

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    int progam_id = ds.storeNewProgram(program);
                    customer.put("customer_program_id", progam_id);
                    ds.storeNewCustomer(customer);
                    
                    tx.commit();
                });

                getResponse().redirect("/admin/");
                flashMessage("The customer has been created");
                Spark.halt();
            }
        }

        return renderTemplate("admin/new-customer");
    }

    private DataItem createCustomerForDB() {

        Request r = getRequest();
        DataItem d = new DataItem();

        String pwHash = Security.hashPassword(r.queryParams("userPassword"));

        d.put("customer_first_name", r.queryParams("firstName"));
        d.put("customer_last_name", r.queryParams("lastName"));
        d.put("customer_email", r.queryParams("userEmail"));
        d.put("customer_pw", pwHash);
        d.put("customer_sex", "m");

        return d;
    }

    private DataItem createProgramForCustomer(String customerName) {

        DataItem it = new DataItem();
        it.put("program_name", "Program for " + customerName);
        it.put("program_description", "Training Program for " + customerName);

        return it;
    }

    private void validateFormInput() throws SQLException {

        checkRequiredValues();

        checkEmailNotUsed();

    }

    private void checkRequiredValues() {
        String[] requiredParams = new String[]{
            "firstName", "lastName", "userEmail", "userPassword"};

        Request req = getRequest();

        for (String param : requiredParams) {
            if (req.queryParams(param).isEmpty()) {
                addValidationError(param + " is required");
            }
        }
    }

    private void checkEmailNotUsed() throws SQLException {
        String email = getRequest().queryParams("userEmail");
        DataItem user = getDataSource().getCustomerByUsername(email);

        if (user != null) {
            addValidationError("The email is already in use");
        }
    }
}
