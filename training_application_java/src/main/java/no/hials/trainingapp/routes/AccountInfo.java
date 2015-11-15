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
        
        DataItem userinfo = getDataSource().getCustomerById(getCurrentUser().getId());
        setData("userinfo", userinfo);
        
        int currentUser = getCurrentUser().getId();
        
        if (getRequest().requestMethod().equals("POST")) {
            
            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                //New First Name
                String cFN = htmlStringValue("customerFirstName");
                if (!cFN.equals("")) {
                    ds.changeCustomerFirstName(currentUser, cFN);
                }

                //New Last Name
                String cLN = htmlStringValue("customerLastName");
                if (!cLN.equals("")) {
                    ds.changeCustomerLastName(currentUser, cLN);
                }

                //New Weight
                int cW = htmlIntValue("customerWeight");
                ds.changeCustomerWeight(currentUser, cW);

                //New Height
                int cH = htmlIntValue("customerHeight");
                ds.changeCustomerHeight(currentUser, cH);

                //New Sex
                String cS = htmlStringValue("customerSex");
                System.out.println(cS);
                if (cS != null) {
                    ds.changeCustomerSex(currentUser, cS);
                }
                //New Password
                String cOP = htmlStringValue("customerOldPassword");
                String cNP = htmlStringValue("customerNewPassword");
                
                Boolean passwordCheck = Security.checkPassword(cOP, userinfo.getString("customer_pw"));
                if (passwordCheck && cNP != null) {
                    String encryptedNewPassword = Security.hashPassword(cNP);
                    ds.changeCustomerPassword(currentUser, encryptedNewPassword);
                }

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
