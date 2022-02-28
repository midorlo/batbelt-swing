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
package de.javagl.common;

import java.util.Objects;

/**
 * A generic implementation of an immutable 2-tuple
 *
 * @param <S> The type of the first element
 * @param <T> The type of the second element
 */
public final class Tuple2<S, T>
{
    /**
     * Creates a new 2-tuple consisting of the given elements
     * 
     * @param <S> The type of the first element
     * @param <SS> A subtype of the first element type
     * @param <T> The type of the second element
     * @param <TT> A subtype of the second element type
     * @param s The first element
     * @param t The second element
     * @return The new tuple
     */
    public static <S, SS extends S, T, TT extends T> Tuple2<S, T> of(SS s, TT t)
    {
        return new Tuple2<S, T>(s, t);
    }
    
    /**
     * The first element
     */
    private final S first;
    
    /**
     * The second element
     */
    private final T second;
    
    /**
     * Creates a new tuple consisting of the given element
     * 
     * @param first The first element
     * @param second The second element
     */
    private Tuple2(S first, T second)
    {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Returns the first element of this tuple
     * 
     * @return The first element of this tuple
     */
    public S getFirst()
    {
        return first;
    }
    
    /**
     * Returns the second element of this tuple
     * 
     * @return The second element of this tuple
     */
    public T getSecond()
    {
        return second;
    }
    
    @Override
    public String toString()
    {
        return "("+first+","+second+")";
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hashCode(first);
        result = prime * result + Objects.hashCode(second);
        return result;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (getClass() != object.getClass())
        {
            return false;
        }
        Tuple2<?,?> other = (Tuple2<?,?>) object;
        if (!Objects.equals(first, other.first))
        {
            return false;
        }
        if (!Objects.equals(second, other.second))
        {
            return false;
        }
        return true;
    }
    
}
