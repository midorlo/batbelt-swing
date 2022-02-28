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

import java.util.function.DoubleFunction;

/**
 * Implementation of a double function that delegates to another
 * double function
 * 
 * @param <T> The type of the returned elements 
 */
public final class DoubleFunctionAdapter<T> implements DoubleFunction<T>
{
    /**
     * The delegate
     */
    private DoubleFunction<? extends T> delegate;
    
    /**
     * Default constructor
     */
    public DoubleFunctionAdapter()
    {
        // Nothing to do here
    }
    
    /**
     * Set the delegate 
     * 
     * @param delegate The delegate
     */
    public void setDelegate(DoubleFunction<? extends T> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public T apply(double value)
    {
        return delegate.apply(value);
    }
    
}
