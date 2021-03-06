/*
 * www.javagl.de - AutoGUI
 *
 * Copyright (c) 2014-2018 Marco Hutter - http://www.javagl.de
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
package de.javagl.autogui.model.properties;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Default implementation of a {@link PropertyAccessor}
 */
final class DefaultPropertyAccessor implements PropertyAccessor
{
    /**
     * The name of the property
     */
    private final String name;
    
    /**
     * The type of the property
     */
    private final Class<?> type;
    
    /**
     * The read method for the property
     */
    private final Function<Object, Object> readMethod;
    
    /**
     * The write method for the property
     */
    private final BiConsumer<Object, Object> writeMethod;
    
    /**
     * Default constructor
     * 
     * @param name The name 
     * @param type The type
     * @param readMethod The read method 
     * @param writeMethod The write method
     */
    DefaultPropertyAccessor(
        String name, Class<?> type, 
        Function<Object, Object> readMethod,
        BiConsumer<Object, Object> writeMethod)
    {
        this.name = Objects.requireNonNull(name, "The name may not be null");
        this.type = Objects.requireNonNull(type, "The type may not be null");
        this.readMethod = Objects.requireNonNull(readMethod, 
            "The readMethod may not be null");
        this.writeMethod = Objects.requireNonNull(writeMethod, 
            "The writeMethod may not be null");
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

    @Override
    public Function<Object, Object> getReadMethod()
    {
        return readMethod;
    }

    @Override
    public BiConsumer<Object, Object> getWriteMethod()
    {
        return writeMethod;
    }
}