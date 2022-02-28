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

import java.util.function.DoubleUnaryOperator;

/**
 * Utility methods to create {@link DoubleUnaryOperator} instances
 */
public class DoubleUnaryOperators
{
    /**
     * Returns a {@link DoubleUnaryOperator} that maps the interval [0,1]
     * to the interval [min,max]
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @return The {@link DoubleUnaryOperator}
     */
    public static DoubleUnaryOperator interpolate(
        final double min, final double max)
    {
        final double delta = max - min;
        return x -> min + x * delta;
    }

    /**
     * Returns a {@link DoubleUnaryOperator} that maps the interval 
     * [minSource,maxSource] to the interval [minTarget,maxTarget], 
     * interpolating linearly.<br>
     * <br>
     * The caller is responsible for passing sensible values. 
     * Particularly, the <code>minSource</code> and <code>maxSource</code> 
     * value should have a reasonable difference.
     * 
     * @param minSource The minimum source value
     * @param maxSource The maximum source value
     * @param minTarget The minimum target value
     * @param maxTarget The maximum target value
     * @return The {@link DoubleUnaryOperator}
     */
    public static DoubleUnaryOperator interpolate(
        final double minSource, final double maxSource,
        final double minTarget, final double maxTarget)
    {
        final double invDeltaSource = 1.0 / (maxSource - minSource);
        final double deltaTarget = maxTarget - minTarget;
        return new DoubleUnaryOperator()
        {
            @Override
            public double applyAsDouble(double value)
            {
                double alpha = (value - minSource) * invDeltaSource;
                return minTarget + alpha * deltaTarget;
            }
        };
    }
    
    /**
     * Returns a {@link DoubleUnaryOperator} that clamps the results from the
     * given function to the interval [min,max]
     * 
     * @param function The function to clamp 
     * @param min The minimum value
     * @param max The maximum value
     * @return The {@link DoubleUnaryOperator}
     */
    public static DoubleUnaryOperator clamp(
        final DoubleUnaryOperator function,
        final double min, final double max)
    {
        return x -> Math.max(min, Math.min(max, function.applyAsDouble(x)));
    }

    /**
     * Returns a {@link DoubleUnaryOperator} that is an affine transformation,
     * returning <code>factor * value + addend</code>.
     * 
     * @param factor The factor
     * @param addend The addend
     * @return The {@link DoubleUnaryOperator}
     */
    public static DoubleUnaryOperator affine(
        final double factor, final double addend)
    {
        return x -> factor * x + addend;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private DoubleUnaryOperators()
    {
        // Private constructor to prevent instantiation
    }


}


