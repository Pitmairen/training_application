package no.hials.trainingapp.routes;

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
 *
 * @author tor-martin
 */
public class AccountInfo extends FormRoute {

    public AccountInfo(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws Exception {
        if (getRequest().requestMethod().equals("POST")) {

            DataItem d = new DataItem();
            Request r = getRequest();
            int currentUser = getCurrentUser().getId();

            d.put("customer_first_name", r.queryParams("customerFirstName"));
            d.put("customer_last_name", r.queryParams("customerLastName"));

            d.put("customer_weight", r.queryParams("customerWeight"));
            d.put("customer_height", r.queryParams("customerHeight"));

            d.put("customer_old_password", r.queryParams("customerOldPassword"));
            d.put("customer_new_password", r.queryParams("customerNewPassword"));

            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {
                if (!d.getString("customer_first_name").equals("")) {
                    ds.changeCustomerFirstName(currentUser, d.getString("customer_first_name"));
                }

                if (!d.getString("customer_last_name").equals("")) {
                    ds.changeCustomerLastName(currentUser, d.getString("customer_last_name"));
                }

                if (!d.getString("customer_weight").equals("")) {
                    ds.changeCustomerWeight(currentUser, d.getInteger("customer_weight"));
                }
                if (!d.getString("customer_height").equals("")) {
                    ds.changeCustomerHeight(currentUser, d.getInteger("customer_height"));
                }

                if (!d.getString("customer_old_password").equals("") && !d.getString("customer_new_password").equals("")) {
                    d.put("customer_old_password", Security.hashPassword(r.queryParams("customerOldPassword")));
//                
                    if (d.getString("customer_old_password").equals(ds.getCustomerPassword(currentUser).getString("1"))) {
                        ds.changeCustomerPassword(currentUser, d.getString("customer_new_password"));
                    }
                }
                tx.commit();

            });

            getResponse().redirect("/");
            Spark.halt();

        }

        return renderTemplate("/account-info");
    }
}
