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
import java.util.function.Function;

/**
 * An iterator over the results of applying a {@link Function} to the
 * values returned by another iterator.
 *
 * @param <S> The input type
 * @param <T> The output type
 */
class TransformingIterator<S, T> implements Iterator<T>
{
    /**
     * The backing iterator
     */
	private final Iterator<? extends S> iterator;
	
	/**
	 * The {@link Function} that will be applied to the
	 * elements of the backing iterator.
	 */
	private final Function<? super S, ? extends T> function;
	
	/**
	 * Creates a new transforming iterator that applies the given 
	 * function to the elements returned by the given iterator.<br>
	 * <br>
	 * The given arguments are assumed to be non-<code>null</code>
	 * 
	 * @param iterator The backing iterator
	 * @param function The {@link Function} to apply to the elements
	 * of the backing iterator
	 */
	TransformingIterator(
	    Iterator<? extends S> iterator, 
	    Function<? super S, ? extends T> function)
	{
		this.iterator = iterator;
		this.function = function;
	}

	@Override
	public boolean hasNext() 
	{
		return iterator.hasNext();
	}

	@Override
	public T next() 
	{
		return function.apply(iterator.next());
	}

	@Override
	public void remove() 
	{
		iterator.remove();
	}
}