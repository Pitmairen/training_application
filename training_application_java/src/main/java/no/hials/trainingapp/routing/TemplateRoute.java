package no.hials.trainingapp.routing;

import java.util.HashMap;
import java.util.Map;
import no.hials.trainingapp.datasource.DataSource;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public abstract class TemplateRoute extends BaseRoute
{

    private final Map<String, Object> mTemplateData;

    public TemplateRoute(DataSource datasource, Request req, Response resp)
    {
        super(datasource, req, resp);
        mTemplateData = new HashMap<>();
    }

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

    public ModelAndView get() throws Exception
    {
        throw new MethodNotSupportedException();
    }

    public ModelAndView post() throws Exception
    {
        throw new MethodNotSupportedException();
    }

    protected void setData(String name, Object value)
    {
        mTemplateData.put(name, value);
    }

    public ModelAndView renderTemplate(String name)
    {
        setData("currentUser", getCurrentUser());

        getResponse().type("text/html");
        return new ModelAndView(mTemplateData, name);
    }
}
