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

import java.util.function.*;

/**
 * Utility methods for creating {@link ToDoubleBiFunction} instances.
 */
public class ToDoubleBiFunctions
{
    /**
     * Returns a view on the given BiFunction as a ToDoubleBiFunction that 
     * thus unboxing the return type from a <code>Number</code> 
     * (like <code>Double</code>) to <code>double</code>
     *  
     * @param <T> The first argument type
     * @param <U> The second argument type
     * @param <R> The return value type
     * @param f The function
     * @param returnValueForNull The return value that should be used
     * when the given delegate function returns <code>null</code>
     * @return The unboxed function
     */
    public static <T, U, R extends Number> ToDoubleBiFunction<T, U> unbox(
        final BiFunction<T, U, R> f, final double returnValueForNull)
    {
        return new ToDoubleBiFunction<T, U>()
        {
            @Override
            public double applyAsDouble(T t, U u)
            {
                Number result = f.apply(t, u);
                if (result == null)
                {
                    return returnValueForNull;
                }
                return result.doubleValue();
            }
        };
    }
    
    /**
     * Composes the given functions
     * 
     * @param <T> The first argument type
     * @param <U> The second argument type
     * @param <R> The first result type
     * 
     * @param biFunction The {@link BiFunction}
     * @param function The {@link ToDoubleFunction}
     * @return The resulting {@link ToDoubleBiFunction}
     */
    public static <T, U, R> ToDoubleBiFunction<T, U> compose(
        final BiFunction<? super T, ? super U, ? extends R> biFunction, 
        final ToDoubleFunction<? super R> function)
    {
        return (x,y) -> function.applyAsDouble(biFunction.apply(x, y));
    }

    /**
     * Composes the given functions
     * 
     * @param <T> The first argument type
     * @param <U> The second argument type
     * @param <R> The result type
     * 
     * @param biFunction The {@link ToDoubleBiFunction}
     * @param function The {@link DoubleFunction}
     * @return The resulting {@link BiFunction}
     */
    public static <T, U, R> BiFunction<T, U, R> compose(
        final ToDoubleBiFunction<? super T, ? super U> biFunction, 
        final DoubleFunction<? extends R> function)
    {
        return (x,y) -> function.apply(biFunction.applyAsDouble(x, y));
    }

    /**
     * Composes the given functions
     * 
     * @param <T> The first argument type
     * @param <U> The second argument type
     * @param <R> The first result type
     * 
     * @param biFunction The {@link ToDoubleBiFunction}
     * @param operator The {@link DoubleUnaryOperator}
     * @return The resulting {@link ToDoubleBiFunction}
     */
    public static <T, U, R> ToDoubleBiFunction<T, U> compose(
        final ToDoubleBiFunction<? super T, ? super U> biFunction, 
        final DoubleUnaryOperator operator)
    {
        return (x,y) -> operator.applyAsDouble(biFunction.applyAsDouble(x, y));
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ToDoubleBiFunctions()
    {
        // Private constructor to prevent instantiation
    }
}
