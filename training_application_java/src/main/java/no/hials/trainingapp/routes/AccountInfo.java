package no.hials.trainingapp.routes;

import no.hials.trainingapp.auth.Security;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormInput;
import no.hials.trainingapp.routing.FormRoute;
import no.hials.trainingapp.routing.Validators;
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
//<<<<<<< HEAD
//        
//        DataItem userinfo = getDataSource().getCustomerById(getCurrentUser().getId());
//        setData("userinfo", userinfo);
//        
//        int currentUser = getCurrentUser().getId();
//        
//        if (getRequest().requestMethod().equals("POST")) {
//            
//            getDataSource().runTransaction((Transaction tx, DataSource ds) -> {
//
//                //New First Name
//                String cFN = htmlStringValue("customerFirstName");
//                if (!cFN.equals("")) {
//                    ds.changeCustomerFirstName(currentUser, cFN);
//                }
//
//                //New Last Name
//                String cLN = htmlStringValue("customerLastName");
//                if (!cLN.equals("")) {
//                    ds.changeCustomerLastName(currentUser, cLN);
//                }
//
//                //New Weight
//                int cW = htmlIntValue("customerWeight");
//                ds.changeCustomerWeight(currentUser, cW);
//
//                //New Height
//                int cH = htmlIntValue("customerHeight");
//                ds.changeCustomerHeight(currentUser, cH);
//
//                //New Sex
//                String cS = htmlStringValue("customerSex");
//                System.out.println(cS);
//                if (cS != null) {
//                    ds.changeCustomerSex(currentUser, cS);
//                }
//                //New Password
//                String cOP = htmlStringValue("customerOldPassword");
//                String cNP = htmlStringValue("customerNewPassword");
//                
//                Boolean passwordCheck = Security.checkPassword(cOP, userinfo.getString("customer_pw"));
//                if (passwordCheck && cNP != null) {
//                    String encryptedNewPassword = Security.hashPassword(cNP);
//                    ds.changeCustomerPassword(currentUser, encryptedNewPassword);
//                }
//
////                    if (d.getString("customer_old_password").equals(ds.getCustomerPassword(currentUser).getString("1"))) {
////                        ds.changeCustomerPassword(currentUser, d.getString("customer_new_password"));
////                    }
////                }
//                tx.commit();
//                
//            });
//            
//        }
//        
//        return renderTemplate("/account-info");
//    }
//    
//    private String htmlStringValue(String name) {
//        String toReturn = getRequest().queryParams(name);
//        System.out.println(toReturn);
//        
//        return toReturn;
//    }
//    
//    private int htmlIntValue(String name) {
//        int toReturn = Integer.parseInt(getRequest().queryParams(name));
//        System.out.println(toReturn);
//        
//        return toReturn;


        DataItem currentUser = getDataSource().getCustomerById(getCurrentUser().getId());

        FormInput form = setupFormValidation(currentUser);

        if (getRequest().requestMethod().equals("POST")) {

            // Populate with form data so the the values are re-displayed
            // in the form in the case there are validation errors.
            populateCustomerDataWithFormValues(currentUser, form);

            if (form.validateInput()) {

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    int uid = currentUser.getInteger("customer_id");

                    ds.changeCustomerFirstName(uid, currentUser.getString("customer_first_name"));
                    ds.changeCustomerLastName(uid, currentUser.getString("customer_last_name"));
                    ds.changeCustomerHeight(uid, currentUser.getInteger("customer_height"));
                    ds.changeCustomerWeight(uid, currentUser.getInteger("customer_weight"));
                    ds.changeCustomerSex(uid, currentUser.getString("customer_sex"));
                    ds.changeCustomerSex(uid, currentUser.getString("customer_sex"));

                    String newPass = form.getValue("customerNewPassword", "");

                    if (!newPass.isEmpty()) {
                        ds.changeCustomerPassword(uid, Security.hashPassword(newPass));
                    }

                    tx.commit();

                });

                flashMessage("Your account information has been updated.");
                getResponse().redirect("/");
                Spark.halt();

            }
        }

        setData("customerData", currentUser);

        return renderTemplate("/account-info");
    }

    private FormInput setupFormValidation(DataItem customerData) {

        FormInput form = getFormInput();

        form.addRequiredInputs("customerFirstName", "customerLastName",
                "customerHeight", "customerWeight", "customerSex");

        form.addValidator("customerHeight", new Validators.IntegerRange(40, 300));
        form.addValidator("customerWeight", new Validators.IntegerRange(20, 400));
        form.addValidator("customerSex", new Validators.ValidValues("m", "f"));

        form.addValidator((FormInput _not_used) -> {

            String newPass = form.getValue("customerNewPassword", "");
            String newPassConfirm = form.getValue("customerNewPasswordConfirm", "");

            if (newPass.isEmpty() && newPassConfirm.isEmpty()) {
                // not changing password so nothing to do
                return;
            }

            if (!newPass.equals(newPassConfirm)) {
                form.addValidationError("The new passwords must match");
                return;
            }

            String oldPass = form.getValue("customerOldPassword", "");
            if (!Security.checkPassword(oldPass, customerData.getString("customer_pw"))) {
                form.addValidationError("The old password was wrong");
            }
        });

        return form;

    }

    private void populateCustomerDataWithFormValues(DataItem data, FormInput form) {

        data.put("customer_first_name", form.getValue("customerFirstName"));
        data.put("customer_last_name", form.getValue("customerLastName"));
        data.put("customer_sex", form.getValue("customerSex"));

        try {
            data.put("customer_height", Integer.parseInt(form.getValue("customerHeight")));
            data.put("customer_weight", Integer.parseInt(form.getValue("customerWeight")));
        } catch (NumberFormatException e) {

        }

    }

}
