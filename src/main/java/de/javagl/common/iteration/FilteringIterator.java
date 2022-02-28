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
import java.util.function.Predicate;

/**
 * An iterator that provides the results from another iterator,
 * filtered with a <code>Predicate</code>
 *
 * @param <T> The type of the elements
 */
class FilteringIterator<T> implements Iterator<T>
{
    /**
     * The backing iterator
     */
    private final Iterator<? extends T> iterator;
    
    /**
     * The predicate that will be applied to the
     * elements of the backing iterator.
     */
    private final Predicate<? super T> predicate;
    
    /**
     * The next element to be returned
     */
    private T next;
    
    /**
     * Whether the next element is valid. (Note that the
     * {@link #next} element may be <code>null</code>, 
     * if this was delivered by the delegate and matched
     * the predicate)
     */
    private boolean hasNext;
    
    /**
     * Creates a new transforming iterator that returns only the elements
     * of the given iterator for which the predicate applies.<br>
     * <br>
     * The given arguments are assumed to be non-<code>null</code>
     * 
     * @param iterator The backing iterator
     * @param predicate The predicate to apply to the elements
     * of the backing iterator. Only the elements that match this
     * predicate will be returned.
     */
    FilteringIterator(
        Iterator<? extends T> iterator, 
        Predicate<? super T> predicate)
    {
        this.iterator = iterator;
        this.predicate = predicate;
        prepareNext();
    }
    
    /**
     * Prepare the next element to be returned by this iterator
     */
    private void prepareNext()
    {
        hasNext = false;
        while (iterator.hasNext())
        {
            next = iterator.next();
            if (predicate.test(next))
            {
                hasNext = true;
                break;
            }
        }
    }
    
    @Override
    public boolean hasNext() 
    {
        return hasNext;
    }

    @Override
    public T next() 
    {
        if (!hasNext)
        {
            throw new NoSuchElementException("No more elements");
        }
        T result = next;
        prepareNext();
        return result;
    }

    @Override
    public void remove() 
    {
        throw new UnsupportedOperationException(
            "May not remove elements with this iterator");
    }
}