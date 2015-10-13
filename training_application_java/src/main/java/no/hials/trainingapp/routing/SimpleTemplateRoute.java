package no.hials.trainingapp.routing;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * A simple template route is used for routes that only needs to render a
 * template without any special logic.
 *
 * The name of the template is specified in the constructor and this template
 * will be rendered when a request comes for this route.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class SimpleTemplateRoute implements TemplateViewRoute
{

    private final String mTemplateName;

    /**
     * Creates a new simple template route with the specified template
     *
     * @param templateName the template name for this route
     */
    public SimpleTemplateRoute(String templateName)
    {
        mTemplateName = templateName;
    }

    /**
     * Handles the request
     *
     * @param request  the current request
     * @param response the current response
     *
     * @return a model and view for the template system.
     */
    @Override
    public ModelAndView handle(Request request, Response response)
    {
        return new SimpleTemplate(request, response).handle();
    }

    /**
     * Extend the template route to make any template data defined in the
     * {@code TemplateRoute} available when the templates is rendered.
     */
    private class SimpleTemplate extends TemplateRoute
    {

        public SimpleTemplate(Request req, Response resp)
        {
            // This handler doesn't need the data source so we just pass null
            super(null, req, resp);
        }

        /**
         * Render the template
         */
        @Override
        public ModelAndView handle()
        {
            return renderTemplate(mTemplateName);
        }

    }

}
