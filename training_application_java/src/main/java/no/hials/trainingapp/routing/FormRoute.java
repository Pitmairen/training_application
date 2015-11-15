package no.hials.trainingapp.routing;

import java.util.HashMap;
import java.util.Map;
import no.hials.trainingapp.datasource.DataSource;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * A form route is a route that is used to handle a HTML form submitted by the
 * user
 *
 * This class adds two values to the template data which is validationErrors and
 * formData. The validation errors is a list of errors that are found after
 * processing the form. And form data is a map of the request data which is sent
 * by the form when it is submitted. These can be used to show a error message
 * to the user and re-populate the HTML form with the previous data in case if
 * validation errors.
 */
public abstract class FormRoute extends TemplateRoute {

    private final FormInput mFormInput;

    public FormRoute(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
        mFormInput = new FormInput(req);
    }

    /**
     * Returns the form input processor object
     *
     * @return the form input processor
     */
    protected FormInput getFormInput() {
        return mFormInput;
    }

    /**
     * Overrides the renderTemplate method to add extra data to the template.
     *
     * The extra template data values is validationErros and formData.
     *
     * @param name the name of the template
     *
     * @return a model and view object used by Spark's template system
     */
    @Override
    protected ModelAndView renderTemplate(String name) {

        setData("validationErrors", mFormInput.getValidationErrors());
        setData("formData", createFormDataMap());

        return super.renderTemplate(name);

    }

    /**
     * Creates the form data map used by the template
     *
     * @return map of the form data
     */
    private Map<String, String> createFormDataMap() {

        Map<String, String> data = new HashMap<>();
        Request req = getRequest();
        for (String param : req.queryParams()) {
            data.put(param, req.queryParams(param));
        }

        return data;
    }
}
