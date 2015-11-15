package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.hials.trainingapp.Config;
import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.auth.Security;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.FormInput;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Login a user
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Login extends FormRoute {

    private final static String TEMPLATE_NAME = "login";

    // Used to cache the user object so we don't have to reload it again
    // after checking the username and password
    private DataItem mUserCache = null;

    private boolean mIsAdminLogin = false;
    private final String mAdminUsername;
    private final String mAdminPassword;

    public Login(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);

        mAdminUsername = Config.getValue("ADMIN_USERNAME", "admin");
        // If no password is set in the config we just use the string
        // "invalid" which will never validate as a correct password.
        mAdminPassword = Config.getValue("ADMIN_PASSWORD", "invalid");

    }

    @Override
    public ModelAndView handle() throws SQLException {

        checkUserNotAuthenticated();

        FormInput form = setupFormValidation();

        if (form.postedAndValid()) {

            if (mIsAdminLogin) {
                Auth.loginAdmin(getRequest());
                getResponse().redirect("/admin/");
            } else {
                Auth.loginUser(getRequest(),
                        mUserCache.getInteger("customer_id"),
                        mUserCache.getString("customer_first_name"));

                getResponse().redirect("/");
            }

            Spark.halt();

        }

        return renderTemplate(TEMPLATE_NAME);
    }

    /**
     * If user is already logged in there is no reason for him to log in so
     * redirect to the index page
     */
    private void checkUserNotAuthenticated() {
        if (getCurrentUser().isAuthenticated()) {
            getResponse().redirect("/");
            Spark.halt();
        }
    }

    private FormInput setupFormValidation() {

        FormInput form = getFormInput();

        form.addRequiredInputs("username", "password");

        form.addValidator((FormInput) -> {

            if(form.hasValidationErrors()){
                return;
            }
            
            String username = form.getValue("username");
            String password = form.getValue("password");

            if (username.equals(mAdminUsername) && Security.checkPassword(password, mAdminPassword)) {
                // If its a valid admin login we are done
                mIsAdminLogin = true;
                return;
            }

            try {
                DataItem customer = getDataSource().getCustomerByUsername(username);

                if (customer != null && Security.checkPassword(password, customer.getString("customer_pw"))) {
                    mUserCache = customer;
                    return; // The login was valid
                }

            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }

            form.addValidationError("Wrong username or password");

        });

        return form;
    }

}
