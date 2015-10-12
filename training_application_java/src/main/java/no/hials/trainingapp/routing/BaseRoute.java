package no.hials.trainingapp.routing;

import no.hials.trainingapp.auth.Auth;
import no.hials.trainingapp.auth.User;
import no.hials.trainingapp.datasource.DataSource;
import spark.Request;
import spark.Response;

/**
 * The base route adds methods that is common for all routes
 *
 * @author Per Myren <progrper@gmail.com>
 */
public abstract class BaseRoute
{

    private final Request mRequest;
    private final Response mResponse;
    private final DataSource mDataSource;

    // Used to cache the current user object so we don't have to 
    // create a new object each time the {@code getCurrentUser} methods
    // is called.
    private User mUserCache = null;

    /**
     * @param datasource the data source object
     * @param req        the current request object
     * @param resp       the current response object
     */
    public BaseRoute(DataSource datasource, Request req, Response resp)
    {
        mDataSource = datasource;
        mRequest = req;
        mResponse = resp;
    }

    /**
     * Returns the data source object
     *
     * @return the data source object
     */
    protected DataSource getDataSource()
    {
        return mDataSource;
    }

    /**
     * Returns the current request object
     *
     * @return the request object
     */
    protected Request getRequest()
    {
        return mRequest;
    }

    /**
     * Returns the current response object
     *
     * @return the response object
     */
    protected Response getResponse()
    {
        return mResponse;
    }

    /**
     * Returns the current user object associated with the the current request
     *
     * @return the current user object
     */
    protected User getCurrentUser()
    {
        if (mUserCache == null) {
            mUserCache = Auth.getUser(mRequest);
        }
        return mUserCache;
    }
}
