package no.hials.trainingapp.routing;

import java.util.HashMap;
import java.util.Map;
import no.hials.trainingapp.datasource.DataSource;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * A template route is a route which generates a response by rendering a
 * template.
 *
 * The {@code handle} method returns a {@code ModelAndView} object which is used
 * by Spark's template system to render a template.
 *
 * Sub classes should implement either the {@code get} or the {@code post}
 * method or both depending on which HTTP methods that the handler supports.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public abstract class TemplateRoute extends BaseRoute
{

    // Data to pass to the template
    private final Map<String, Object> mTemplateData;

    /**
     * Create a new template route
     *
     * @param datasource the data source
     * @param req        the current request
     * @param resp       the current response
     */
    public TemplateRoute(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
        mTemplateData = new HashMap<>();
    }

    /**
     * Handle the current request.
     *
     * This method will delegate the handling to either the {@code get} or the
     * {@code post} method depending on the HTTP request method.
     *
     * @return a mode and view object for Spark's template system
     *
     * @throws Exception
     */
    public ModelAndView handle() throws Exception
    {
        switch (getRequest().requestMethod()) {

            case "GET":
                return get();
            case "POST":
                return post();
            default:
                throw new MethodNotSupportedException();

        }
    }

    /**
     * Handles GET requests. Sub classes should override this method if the
     * method is supported.
     *
     * @return a mode and view object for Spark's template system
     *
     * @throws Exception
     */
    protected ModelAndView get() throws Exception
    {
        throw new MethodNotSupportedException();
    }

    /**
     * Handles POST requests. Sub classes should override this method if the
     * method is supported.
     *
     * @return a mode and view object for Spark's template system
     *
     * @throws Exception
     */
    protected ModelAndView post() throws Exception
    {
        throw new MethodNotSupportedException();
    }

    /**
     * Adds data which will be passed to the template when it is rendered.
     *
     * @param name  the variable name which the template will use to access the
     *              data
     * @param value the data value
     */
    protected void setData(String name, Object value)
    {
        mTemplateData.put(name, value);
    }

    /**
     * Renders the template with the specified name.
     *
     * Any template data added using {@code setData} will be passed to the
     * template and will be available to the template when it is rendered.
     *
     * @param name the name of the template without extension.
     *
     * @return a mode and view object for Spark's template system
     */
    protected ModelAndView renderTemplate(String name)
    {
        // Add the current user
        setData("currentUser", getCurrentUser());
        // Response data is html
        getResponse().type("text/html");

        return new ModelAndView(mTemplateData, name);
    }
}
