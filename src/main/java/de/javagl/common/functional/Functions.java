/*
 * www.javagl.de - Common
 *
 * Copyright (c) 2012-2014 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package de.javagl.common.functional;

import java.util.*;
import java.util.function.Function;

/**
 * Utility methods for creating {@link Function} instances.
 */
public class Functions
{
    /**
     * Creates a new <code>Function</code> from the given list. <br>
     * <br>
     * The function will use given integer argument as the index for
     * the list. If the given integer argument is not in bounds, then
     * the given default value will be returned.<br>
     * <br>
     * The function will be a <i>view</i> on the given list. So changes in
     * the list will be visible via the returned function.
     * 
     * @param <T> The value type
     * @param list The list
     * @param defaultValue The default value that will be returned when
     * the index that is given as the function argument is out of bounds.
     * @return The {@link Function}
     */
    static <T> Function<Integer, T> fromList(
        final List<T> list, T defaultValue)
    {
        return new Function<Integer, T>()
        {
            @Override
            public T apply(Integer i)
            {
                if (i >= 0 && i<list.size())
                {
                    return list.get(i);
                }
                return defaultValue;
            }
        };
    }
    
    /**
     * Returns a {@link Function} that is the inverse of the given 
     * {@link Function}, defined for the given arguments. For a
     * given function <code>f : S -&gt; T</code> and a set of arguments 
     * <code>S<sub>0</sub></code>, the returned function will be a 
     * function that maps each <code>t</code> from 
     * <code>T<sub>0</sub> = f(S<sub>0</sub>)</code> to the <code>s</code> from
     * <code>S<sub>0</sub></code> for which <code>f(s) = t</code>. 
     * If the given function is not injective (that is, when multiple
     * arguments are mapped to the same value), then the resulting 
     * function will return the last argument that was mapped to the
     * respective ambiguous value, based on the order of the given 
     * arguments sequence.
     * 
     * @param <S> The argument type
     * @param <T> The value type
     * @param function The {@link Function}
     * @param arguments The arguments
     * @return The inverse {@link Function}
     */
    public static <S, T> Function<T, S> invert(
        Function<? super S, ? extends T> function, 
        Iterable<? extends S> arguments)
    {
        Map<T, S> map = new LinkedHashMap<T, S>();
        for (S s : arguments)
        {
            T t = function.apply(s);
            map.put(t, s);
        }
        return k -> map.get(k);
    }

    
    /**
     * Create a {@link Function} that is backed by the given map.
     * Modifications of the given map will be visible through
     * the returned function.
     * 
     * @param <K> The key type
     * @param <V> The value type
     * @param map The backing map
     * @param defaultValue The default value to return when a
     * function argument is not contained in the map. 
     * @return The new function
     */
    public static <K, V> Function<K, V> fromMap(
        final Map<? super K, ? extends V> map, final V defaultValue)
    {
        return new Function<K, V>()
        {
            @Override
            public V apply(K k)
            {
                V result = map.get(k);
                if (result == null)
                {
                    if (!map.containsKey(k))
                    {
                        return defaultValue;
                    }
                }
                return result;
            }
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Functions()
    {
        // Private constructor to prevent instantiation
    }
}
