package no.hials.trainingapp.routing;

import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.auth.User;
import no.hials.trainingapp.datasource.DataSource;
import spark.Request;
import spark.Response;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public abstract class BaseRoute
{

    private final Request mRequest;
    private final Response mResponse;
    private final DataSource mDataSource;

    private User mUserCache = null;

    public BaseRoute(DataSource datasource, Request req, Response resp)
    {
        mDataSource = datasource;
        mRequest = req;
        mResponse = resp;
    }

    /**
     * @return the data source
     */
    protected DataSource getDataSource()
    {
        return mDataSource;
    }

    /**
     * @return the mRequest
     */
    protected Request getRequest()
    {
        return mRequest;
    }

    /**
     * @return the mResponse
     */
    protected Response getResponse()
    {
        return mResponse;
    }

    /**
     * @return the mResponse
     */
    protected User getCurrentUser()
    {
        if (mUserCache == null) {
            mUserCache = Auth.getUser(mRequest);
        }
        return mUserCache;
    }
}
