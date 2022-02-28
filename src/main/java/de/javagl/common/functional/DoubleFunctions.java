/*
 * www.javagl.de - Common
 *
 * Copyright (c) 2012-2015 Marco Hutter - http://www.javagl.de
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

import java.util.function.DoubleFunction;
import java.util.function.Function;

/**
 * Methods related to DoubleFunctions and Functions that return Double 
 * values 
 */
public class DoubleFunctions
{
    /**
     * Returns a view on the given <code>DoubleFunction</code> as a 
     * <code>Function</code> that accepts a <code>Double</code>, 
     * thus boxing the argument type from <code>double</code> to
     * <code>Double</code>
     *  
     * @param <S> The return value type
     * @param f The function
     * @param returnValueForNullArgument What is <b>returned</b> when the 
     * argument that is given to the resulting function is <code>null</code>.
     * @return The boxed function
     */
    public static <S> Function<Double, S> boxArgument(
        final DoubleFunction<S> f, final S returnValueForNullArgument)
    {
        return x -> x == null ? returnValueForNullArgument : f.apply(x);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private DoubleFunctions()
    {
        // Private constructor to prevent instantiation
    }

}
