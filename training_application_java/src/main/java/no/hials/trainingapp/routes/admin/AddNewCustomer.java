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
public class AddNewCustomer extends FormRoute
{

    public AddNewCustomer(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws SQLException {

        if(getRequest().requestMethod().equals("POST")){
            
            validateFormInput();
            
            if(!hasValidationErrors()){
                DataItem d = new DataItem();
                Request r = getRequest();
                
                String pwHash = Security.hashPassword(r.queryParams("userPassword"));
                
                d.put("customer_first_name", r.queryParams("firstName"));
                d.put("customer_last_name", r.queryParams("lastName"));
                d.put("customer_email", r.queryParams("userEmail"));
                d.put("customer_pw", pwHash);
                d.put("customer_sex", "m");

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {
                    
                    ds.storeNewCustomer(d);
                    tx.commit();
                });
                
                getResponse().redirect("/admin");
                Spark.halt();
            }
        }
        
    
        return renderTemplate("admin/new-customer");
    }
    
    
    private void validateFormInput() throws SQLException{
        
        checkRequiredValues();
        
        checkEmailNotUsed();

        
    }
    
    
    private void checkRequiredValues()
    {
        String[] requiredParams = new String[]{
            "firstName", "lastName", "userEmail", "userPassword"};

        Request req = getRequest();
        
        for(String param : requiredParams){
            if(req.queryParams(param).isEmpty()){
                addValidationError(param + " is required");
            } 
        }        
    }
    
    private void checkEmailNotUsed() throws SQLException
    {
        String email = getRequest().queryParams("userEmail");
        DataItem user = getDataSource().getCustomerByUsername(email);
        
        if(user != null){
            addValidationError("The email is already in use");
        }
    }
}
