/*
 * www.javagl.de - Reflection
 * 
 * Copyright 2013-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.reflection;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods related to lists
 */
class Lists
{
    /**
     * Returns an unmodifiable list containing the elements of the given list,
     * filtered with the given predicates
     * 
     * @param <T> The element type
     * @param list The list
     * @param predicates The predicates
     * @return The filtered list
     */
    @SafeVarargs
    static <T> List<T> filter(
        List<? extends T> list, Predicate<? super T> ... predicates)
    {
        Stream<? extends T> stream = list.stream();
        for (Predicate<? super T> p : predicates)
        {
            stream = stream.filter(p);
        }
        return Collections.unmodifiableList(
            stream.collect(Collectors.<T>toList()));
        //                            ^^^  
        // This is only required for older javac versions (e.g. 1.8.0_31) 
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Lists()
    {
        // Private constructor to prevent instantiation
    }
}
