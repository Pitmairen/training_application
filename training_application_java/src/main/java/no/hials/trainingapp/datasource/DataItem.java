package no.hials.trainingapp.datasource;

import java.util.HashMap;

/**
 * The data item represents a row in a database result
 *
 * The columns can be accessed using the the column name or column label as the
 * key in the map.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class DataItem extends HashMap<String, Object>
{

    private static final long serialVersionUID = 1L;

    /**
     * Returns the value as a integer
     *
     * @param key
     *
     * @return the integer value
     */
    public int getInteger(String key)
    {
        return (int) get(key);
    }

    /**
     * Returns the value as a string
     *
     * @param key
     *
     * @return the string value
     */
    public String getString(String key)
    {
        return (String) get(key);
    }
}
