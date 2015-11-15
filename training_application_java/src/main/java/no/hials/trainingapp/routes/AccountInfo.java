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

        int currentUser = getCurrentUser().getId();

        if (getRequest().requestMethod().equals("POST")) {

            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                //New Weight
                int cW = htmlIntValue("customerWeight"); 
                ds.changeCustomerWeight(currentUser, cW);

                //New Height
                int cH = htmlIntValue("customerHeight");
                ds.changeCustomerHeight(currentUser, cH);

                //New Name
                String cFN = htmlStringValue("customerFirstName");
                String cLN = htmlStringValue("customerLastName");
                ds.changeCustomerName(currentUser, cFN, cLN);

                //New Sex
                String cS = htmlStringValue("customerSex");
                ds.changeCustomerSex(currentUser, cS);

                //New Password
                String cOP = htmlStringValue("customerOldPassword");
                String cNP = htmlStringValue("customerNewPassword");

//                if (!d.getString("customer_old_password").equals("") && !d.getString("customer_new_password").equals("")) {
//                    d.put("customer_old_password", Security.hashPassword(r.queryParams("customerOldPassword")));
//
//                    if (d.getString("customer_old_password").equals(ds.getCustomerPassword(currentUser).getString("1"))) {
//                        ds.changeCustomerPassword(currentUser, d.getString("customer_new_password"));
//                    }
//                }
                tx.commit();

            });

        }

        return renderTemplate("/account-info");
    }

    private String htmlStringValue(String name) {
        String toReturn = getRequest().queryParams(name);
        System.out.println(toReturn);

        return toReturn;
    }

    private int htmlIntValue(String name) {
        int toReturn = Integer.parseInt(getRequest().queryParams(name));
        System.out.println(toReturn);

        return toReturn;
    }
}
