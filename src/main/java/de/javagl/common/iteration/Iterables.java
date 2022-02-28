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
 * Utility methods related to Iterables.<br>
 * <br>
 * Unless otherwise noted, none of the parameters of these methods 
 * may be <code>null</code>.
 */
public class Iterables
{

    /**
     * Drains all elements that are provided by the iterator of
     * the given iterable into a list
     * 
     * @param <T> The element type
     * @param iterable The iterable
     * @return The list
     */
    public static <T> List<T> toList(Iterable<T> iterable)
    {
        Objects.requireNonNull(iterable, "The iterable is null");
        return Iterables.toCollection(iterable, new ArrayList<T>());
    }

    /**
     * Drains all elements that are provided by the iterator of
     * the given iterable into a set
     * 
     * @param <T> The element type
     * @param iterable The iterable
     * @return The set
     */
    public static <T> Set<T> toSet(Iterable<T> iterable)
    {
        Objects.requireNonNull(iterable, "The iterable is null");
        return Iterables.toCollection(iterable, new LinkedHashSet<T>());
    }

    /**
     * Drains all elements that are provided by the iterator of
     * the given iterable into the given collection
     * 
     * @param <T> The element type
     * @param <C> The collection type
     * @param iterable The iterable
     * @param collection The target collection
     * @return The given collection
     */
    public static <T, C extends Collection<? super T>> C toCollection(
        Iterable<T> iterable, C collection)
    {
        Objects.requireNonNull(iterable, "The iterable is null");
        Objects.requireNonNull(collection, "The collection is null");
        return Iterators.toCollection(iterable.iterator(), collection);
    }

    /**
     * Returns an iterator that combines the iterators that are provided 
     * by the iterables that are provided by the iterator of the given
     * iterable. Fancy stuff, he?
     * 
     * @param <T> The element type
     * @param iterablesIterable The iterable over the iterables. May not 
     * provide <code>null</code> iterables.
     * @return The iterator
     */
    public static <T> Iterable<T> iterableOverIterables(
        final Iterable<? extends Iterable<? extends T>> iterablesIterable)
    {
        Objects.requireNonNull(iterablesIterable, 
            "The iterablesIterable is null");
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                return Iterators.iteratorOverIterables(
                    iterablesIterable.iterator());
            }
        };
    }

    
    /**
     * Returns an iterable that provides iterators that are transforming
     * the elements provided by the iterators of the given iterable using
     * the given function
     * 
     * @param <S> The element type 
     * @param <T> The value type 
     * @param iterable The delegate iterable
     * @param function The function
     * @return The iterator
     */
    public static <S, T> Iterable<T> transformingIterable(
        final Iterable<? extends S> iterable, 
        final Function<S, ? extends T> function)
    {
        Objects.requireNonNull(iterable, "The iterable is null");
        Objects.requireNonNull(function, "The function is null");
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                return new TransformingIterator<S, T>(
                    iterable.iterator(), function);
            }
        };
    }

    /**
     * Returns an iterable that provides an iterator that only returns the
     * elements provided by the iterator of the given iterable to which
     * the given predicate applies.
     * 
     * @param <T> The element type 
     * @param iterable The delegate iterable
     * @param predicate The predicate
     * @return The iterator
     */
    public static <T> Iterable<T> filteringIterable(
        final Iterable<? extends T> iterable, 
        final Predicate<? super T> predicate)
    {
        Objects.requireNonNull(iterable, "The iterable is null");
        Objects.requireNonNull(predicate, "The predicate is null");
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                return new FilteringIterator<T>(
                    iterable.iterator(), predicate);
            }
        };
    }
    
    

    /**
     * Private constructor to prevent instantiation
     */
    private Iterables()
    {
        // Private constructor to prevent instantiation
    }
}
