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

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility methods related to iterators.<br>
 * <br>
 * Unless otherwise noted, none of the parameters of these methods 
 * may be <code>null</code>.
 */
public class Iterators
{
    /**
     * Drains all elements from the given iterator into a list
     * 
     * @param <T> The element type 
     * @param iterator The iterator
     * @return The list
     */
    public static <T> List<T> toList(Iterator<T> iterator)
    {
        Objects.requireNonNull(iterator, "The iterator is null");
        return Iterators.toCollection(iterator, new ArrayList<T>());
    }

    /**
     * Drains all elements from the given iterator into a set
     * 
     * @param <T> The element type 
     * @param iterator The iterator
     * @return The set
     */
    public static <T> Set<T> toSet(Iterator<T> iterator)
    {
        Objects.requireNonNull(iterator, "The iterator is null");
        return Iterators.toCollection(iterator, new LinkedHashSet<T>());
    }

    /**
     * Drains all elements that are provided by the given iterator 
     * into the given collection
     * 
     * @param <T> The element type 
     * @param <C> The collection type 
     * @param iterator The iterator
     * @param collection The target collection
     * @return The given collection
     */
    public static <T, C extends Collection<? super T>> C toCollection(
        Iterator<T> iterator, C collection)
    {
        Objects.requireNonNull(iterator, "The iterator is null");
        Objects.requireNonNull(iterator, "The collection is null");
        while (iterator.hasNext())
        {
            collection.add(iterator.next());
        }
        return collection;
    }

    /**
     * Returns an iterator that removes the type bound 
     * of another iterator
     * 
     * @param <T> The element type 
     * @param delegate The delegate iterator
     * @return The new iterator
     */
    public static <T> Iterator<T> weakeningIterator(
        final Iterator<? extends T> delegate)
    {
        Objects.requireNonNull(delegate, "The delegate is null");
        return new Iterator<T>()
        {
            @Override
            public boolean hasNext()
            {
                return delegate.hasNext();
            }
    
            @Override
            public T next()
            {
                return delegate.next();
            }
    
            @Override
            public void remove()
            {
                delegate.remove();
            }
        };
    }

    /**
     * Returns an iterator that combines the given iterators. 
     * 
     * @param <T> The element type 
     * @param iterator0 The first iterator
     * @param iterator1 The second iterator
     * @return The iterator
     */
    public static <T> Iterator<T> iteratorOverIterators(
        Iterator<? extends T> iterator0, Iterator<? extends T> iterator1)
    {
        Objects.requireNonNull(iterator0, "The iterator0 is null");
        Objects.requireNonNull(iterator1, "The iterator1 is null");
        List<Iterator<? extends T>> iterators = 
            new ArrayList<Iterator<? extends T>>(2);
        iterators.add(iterator0);
        iterators.add(iterator1);
        return Iterators.iteratorOverIterators(iterators.iterator());
    }

    /**
     * Returns an iterator that combines the iterators
     * that are returned by the given iterator.
     * 
     * @param <S> The iterator type 
     * @param <T> The element type 
     * @param iteratorsIterator The iterator iterator. May not provide
     * <code>null</code> iterators.
     * @return The iterator
     */
    public static <S extends Iterator<? extends T>, T> Iterator<T> 
        iteratorOverIterators(Iterator<S> iteratorsIterator)
    {
        Objects.requireNonNull(iteratorsIterator, 
            "The iteratorsIterator is null");
        return new CombiningIterator<T>(iteratorsIterator);
    }

    /**
     * Returns an iterator that combines the iterators that 
     * are returned by the iterator that is returned by the
     * given iterable.
     *  
     * @param <S> The iterator type 
     * @param <T> The element type 
     * @param iteratorsIterable The iterable for the iterators. May not
     * provide <code>null</code> iterators.
     * @return The iterator
     */
    public static <S extends Iterator<? extends T>, T> Iterator<T> 
        iteratorOverIterators(Iterable<S> iteratorsIterable)
    {
        Objects.requireNonNull(iteratorsIterable, 
            "The iteratorsIterable is null");
        return new CombiningIterator<T>(iteratorsIterable.iterator());
    }

    /**
     * Returns an iterator that combines the iterators that are
     * returned by the iterables that are provided by the 
     * given iterator
     * 
     * @param <S> The iterable type 
     * @param <T> The element type 
     * @param iterablesIterator The iterator over the iterables.
     * May not provide <code>null</code> iterables
     * @return The iterator
     */
    public static <S extends Iterable<? extends T>, T> Iterator<T> 
        iteratorOverIterables(Iterator<S> iterablesIterator)
    {
        Objects.requireNonNull(iterablesIterator, 
            "The iterablesIterator is null");
        TransformingIterator<S, Iterator<? extends T>> iteratorIterator = 
            new TransformingIterator<S, Iterator<? extends T>>(
                iterablesIterator, iterable -> iterable.iterator());
        return new CombiningIterator<T>(iteratorIterator);
    }

    /**
     * Returns an iterator that combines the iterators that are
     * returned by the iterables that are provided by the iterator
     * that is provided by the given iterable
     * 
     * @param <S> The iterable type 
     * @param <T> The element type 
     * @param iterablesIterable The iterable for the iterables. May not
     * provide <code>null</code> iterables.
     * @return The iterator
     */
    public static <S extends Iterable<? extends T>, T> Iterator<T> 
        iteratorOverIterables(Iterable<S> iterablesIterable)
    {
        Objects.requireNonNull(iterablesIterable, 
            "The iterablesIterable is null");
        return iteratorOverIterables(iterablesIterable.iterator());
    }

    /**
     * Returns an iterator that combines the iterators that are
     * returned by the given iterables
     * 
     * @param <T> The element type 
     * @param iterable0 The first iterable
     * @param iterable1 The second iterable
     * @return The iterator
     */
    public static <T> Iterator<T> iteratorOverIterables(
        Iterable<? extends T> iterable0, Iterable<? extends T> iterable1)
    {
        Objects.requireNonNull(iterable0, "The iterable0 is null");
        Objects.requireNonNull(iterable1, "The iterable1 is null");
        List<Iterable<? extends T>> iterables =
            new ArrayList<Iterable<? extends T>>(2);
        iterables.add(iterable0);
        iterables.add(iterable1);
        return iteratorOverIterables(iterables.iterator());
    }

    /**
     * Creates an iterator that passes the values that are provided
     * by the given delegate iterator to the given function, and
     * returns the resulting values
     * 
     * @param <S> The element type 
     * @param <T> The value type 
     * @param iterator The delegate iterator
     * @param function The function
     * @return The iterator
     */
    public static <S, T> Iterator<T> transformingIterator(
        Iterator<? extends S> iterator, 
        Function<S, ? extends T> function)
    {
        Objects.requireNonNull(iterator, "The iterator is null");
        Objects.requireNonNull(function, "The function is null");
        return new TransformingIterator<S, T>(iterator, function);
    }
    
    /**
     * Creates an iterator that only returns the elements from the
     * given iterator to which the given predicate applies.
     * 
     * @param <T> The element type 
     * @param iterator The delegate iterator
     * @param predicate The predicate
     * @return The iterator
     */
    public static <T> Iterator<T> filteringIterator(
        Iterator<? extends T> iterator, 
        Predicate<? super T> predicate)
    {
        Objects.requireNonNull(iterator, "The iterator is null");
        Objects.requireNonNull(predicate, "The predicate is null");
        return new FilteringIterator<T>(iterator, predicate);
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private Iterators()
    {
        // Private constructor to prevent instantiation
    }
}
