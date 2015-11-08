package no.hials.trainingapp.routing;

import spark.Request;

/**
 * Helper Functions
 */
public class Helpers {

    /**
     * Returns the pagination page for the request.
     *
     * The page number is defined as a ?page=n query string parameter.
     *
     * If it is not specified 1 will be returned.
     *
     * @param req the request object
     * @return the page number
     */
    public static int getPaginationPage(Request req){
        String pageStr = req.queryParams("page");
        try {
            return Integer.parseInt(pageStr);
        }catch (NumberFormatException e){
            return 1;
        }
    }

}
