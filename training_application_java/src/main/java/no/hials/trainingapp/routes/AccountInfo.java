package no.hials.trainingapp.routes;

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
//        if (getRequest().requestMethod().equals("POST")) {
//
//            DataItem d = new DataItem();
//            Request r = getRequest();
//
//            d.put("customer_weight", r.queryParams("customerWeight"));
//
//            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {
//
//                ds.changeCustomerWeight(getCurrentUser().getId(), d.getInteger("customerWeight"));
//                tx.commit();
//            });
//
//            getResponse().redirect("/");
//            Spark.halt();
//
//        }
//
        return renderTemplate("/account-info");
  }
}


