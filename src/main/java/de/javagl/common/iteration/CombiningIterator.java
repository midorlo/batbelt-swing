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

package de.javagl.common.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of an iterator that combines multiple other iterators.
 *
 * @param <T> The type of the elements
 */
class CombiningIterator<T> implements Iterator<T> 
{
    /**
     * The iterator over the delegate iterators
     */
    private final Iterator<? extends Iterator<? extends T>> iteratorIterator;
    
    /**
     * The current iterator
     */
    private Iterator<? extends T> currentIterator;
    
    /**
     * The iterator that may be used in the {@link #remove()} method
     */
    private Iterator<? extends T> removalIterator;
    
    /**
     * Creates an iterator for iterating over the iterators returned
     * by the given iterator.
     * 
     * @param iteratorIterator The iterator iterator. That's what it is.
     */
	CombiningIterator(
	    Iterator<? extends Iterator<? extends T>> iteratorIterator)
	{
		this.iteratorIterator = iteratorIterator;
		prepareNextIterator();
	}
	
	/**
	 * Prepare the next iterator to use
	 */
	private void prepareNextIterator()
	{
	    if (currentIterator == null || !currentIterator.hasNext())
	    {
	        currentIterator = null;
	        while (iteratorIterator.hasNext())
	        {
	            currentIterator = iteratorIterator.next();
	            if (currentIterator.hasNext())
	            {
	                break;
	            }
	            currentIterator = null;
	        }
	    }
	}

	@Override
	public boolean hasNext() 
	{
		if (currentIterator == null)
		{
			return false;
		}
		if (currentIterator.hasNext())
		{
		    return true;
		}
		// Should never happen
		throw new RuntimeException("Invalid iterator state");
	}

	@Override
	public T next() 
	{
		if (currentIterator == null)
		{
		    throw new NoSuchElementException("No more elements");
		}
		T result = currentIterator.next();
		removalIterator = currentIterator;
		prepareNextIterator();
		return result;
	}

	@Override
	public void remove() 
	{
		if (removalIterator == null)
		{
			throw new IllegalStateException("No more elements");
		}
		removalIterator.remove();
		removalIterator = null;
	}
	
}