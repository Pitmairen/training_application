package no.hials.trainingapp.routing;

import java.util.ArrayList;
import java.util.List;
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
public class FormRoute extends TemplateRoute
{

    private final List<String> mValidationErrors;

    public FormRoute(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
        mValidationErrors = new ArrayList<>();
    }

    /**
     * Adds a new error message to the validation errors list
     *
     * @param errorMessage the message to add
     */
    protected void addValidationError(String errorMessage)
    {
        mValidationErrors.add(errorMessage);
    }

    /**
     * Checks if there is any validation errors
     *
     * @return true if there are any validation errors
     */
    protected boolean hasValidationErrors()
    {
        return !mValidationErrors.isEmpty();
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
    protected ModelAndView renderTemplate(String name)
    {

        setData("validationErrors", mValidationErrors);
        setData("formData", getRequest().params());

        return super.renderTemplate(name);

    }
}
