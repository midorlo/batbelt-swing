package de.javagl.common.collections;

import java.util.Map.Entry;
import java.util.NavigableMap;

/**
 * Utility methods related to maps containing <code>Double</code> 
 * keys and values
 */
public class DoubleMaps
{
    /**
     * Returns a linearly interpolated value from the given map.<br>
     * <br>
     * For the given key, the next larger and next smaller key
     * will be determined. The values associated with these keys
     * will be interpolated, based on the location of the given
     * key between the next larger and smaller key.<br>
     * <br>
     * If the given key is larger than the largest key in the
     * given map, then the value of the largest key will be
     * returned.<br>
     * <br>
     * If the given key is smaller than the smallest key in the
     * given map, then the value of the smallest key will be
     * returned.
     *  
     * @param map The map
     * @param key The key
     * @return The interpolated value
     * @throws IllegalArgumentException If the given map is empty
     */
    public static double getInterpolated(
        NavigableMap<Double, ? extends Number> map, double key)
    {
        if (map.isEmpty())
        {
            throw new IllegalArgumentException("Empty map");
        }
        Entry<Double, ? extends Number> c = map.ceilingEntry(key);
        Entry<Double, ? extends Number> f = map.floorEntry(key);
        if (c == null)
        {
            return f.getValue().doubleValue();
        }
        if (f == null)
        {
            return c.getValue().doubleValue();
        }
        if (c.equals(f))
        {
            return c.getValue().doubleValue();
        }
        double deltaKeys = c.getKey() - f.getKey();
        double alpha = (key - f.getKey()) / deltaKeys;
        double deltaValues = 
            c.getValue().doubleValue() - 
            f.getValue().doubleValue();
        double result = f.getValue().doubleValue() + alpha * deltaValues;
        return result;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private DoubleMaps()
    {
        // Private constructor to prevent instantiation
    }

}
