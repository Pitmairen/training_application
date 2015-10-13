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

    // @see http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
    private static final long serialVersionUID = 1L;

    
    /**
     * Returns the value as a integer
     *
     * @param key the key to get
     *
     * @return the integer value associated with the key
     */
    public int getInteger(String key)
    {
        return (int) get(key);
    }

    /**
     * Returns the value as a string
     *
     * @param key the key to get
     *
     * @return the string value associated with the key
     */
    public String getString(String key)
    {
        return (String) get(key);
    }
}
