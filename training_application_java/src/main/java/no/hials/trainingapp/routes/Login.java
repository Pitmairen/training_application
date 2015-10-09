package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Login extends TemplateRoute
{

    private final List<String> mValidationErrors;
    private DataItem mUserCache = null;

    public Login(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);

        mValidationErrors = new ArrayList<>();
    }

    @Override
    public ModelAndView get() throws Exception
    {
        checkAuth();

        return renderTemplate();
    }

    @Override
    public ModelAndView post() throws Exception
    {
        checkAuth();
        
        checkLogin();

        if (mValidationErrors.isEmpty()) {
            Auth.loginUser(getRequest(),
                           mUserCache.getInteger("customer_id"),
                           mUserCache.getString("customer_first_name"));

            getResponse().redirect("/");
            halt();
        }

        return renderTemplate();
    }

    private void checkAuth(){
        if(getCurrentUser().isAuthenticated()){
            getResponse().redirect("/");
            halt();
        }
    }
    
    
    private void checkLogin() throws SQLException
    {

        String username = getRequest().queryParams("username");

        DataItem customer = getDataSource().getCustomerByUsername(username);

        if (customer == null || !username.equals(customer.get("customer_email"))) {
            mValidationErrors.add("Wrong username or password");
        }
        else {
            mUserCache = customer;
        }

    }

    private ModelAndView renderTemplate()
    {

        setData("validationErrors", mValidationErrors);
        setData("formData", getRequest().params());

        return renderTemplate("login");
    }
}
