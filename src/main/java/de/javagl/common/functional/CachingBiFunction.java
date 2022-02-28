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

import de.javagl.common.Tuple2;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Implementation of a BiFunction that internally caches the values
 * that are obtained from a delegate 
 *
 * @param <T> The first argument type
 * @param <U> The second argument type
 * @param <R> The result type
 */
public final class CachingBiFunction<T, U, R> implements BiFunction<T, U, R>
{
    /**
     * The delegate
     */
    private final BiFunction<T, U, R> delegate;
    
    /**
     * The cache
     */
    private final Map<Tuple2<T, U>, R> map;

    /**
     * Creates a new caching BiFunction with the given delegate
     * 
     * @param delegate The delegate
     * @throws NullPointerException If the delegate is <code>null</code>
     */
    public CachingBiFunction(BiFunction<T, U, R> delegate)
    {
        Objects.requireNonNull(delegate);
        this.delegate = delegate;
        this.map = new HashMap<Tuple2<T,U>, R>();
    }
    
    /**
     * Clear the internal cache
     */
    public void clear()
    {
        map.clear();
    }
    
    @Override
    public R apply(T t, U u)
    {
        Tuple2<T, U> key = Tuple2.of(t, u);
        R value = map.get(key);
        if (value == null)
        {
            value = delegate.apply(t, u);
            map.put(key, value);
        }
        return value;
    }
    
    
}


