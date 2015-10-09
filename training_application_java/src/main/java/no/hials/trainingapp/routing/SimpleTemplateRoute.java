package no.hials.trainingapp.routing;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class SimpleTemplateRoute implements TemplateViewRoute
{

    private final String mTemplateName;

    public SimpleTemplateRoute(String templateName)
    {
        mTemplateName = templateName;
    }

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception
    {
        return new SimpleTemplate(request, response).handle();
    }

    private class SimpleTemplate extends TemplateRoute
    {

        public SimpleTemplate(Request req, Response resp)
        {
            super(null, req, resp);
        }

        @Override
        public ModelAndView get()
        {
            return renderTemplate(mTemplateName);
        }

    }

}
