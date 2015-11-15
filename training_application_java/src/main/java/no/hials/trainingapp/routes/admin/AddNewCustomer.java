package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.hials.trainingapp.auth.Security;
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
 * Creates a new customer
 */
public class AddNewCustomer extends FormRoute {

    public AddNewCustomer(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        FormInput form = setupFormValidation();

        if (form.postedAndValid()) {

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

        return renderTemplate("admin/new-customer");
    }

    private DataItem createCustomerForDB() {

        FormInput form = getFormInput();
        DataItem d = new DataItem();
        
        String pwHash = Security.hashPassword(form.getValue("userPassword"));

        d.put("customer_first_name", form.getValue("firstName"));
        d.put("customer_last_name", form.getValue("lastName"));
        d.put("customer_email", form.getValue("userEmail"));
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


    private FormInput setupFormValidation() throws SQLException {
        
        
        FormInput form = getFormInput();
        
        form.addRequiredInputs("firstName", "lastName", "userEmail", "userPassword");
        
        form.addValidator("userEmail", (FormInput form1, String input) -> {

            try {
                
                String email = form.getValue(input);
                DataItem user = getDataSource().getCustomerByUsername(email);
                
                if (user == null) {
                    return;
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(AddNewCustomer.class.getName()).log(Level.SEVERE, null, ex);
            }

            form.addValidationError("The email is already in use");
            
        });
        
        return form;
    }
}
