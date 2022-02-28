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

import java.util.*;

/**
 * Utility methods related to types
 */
class Types
{
    /**
     * The mapping from primitive type names (and void) to the respective
     * class objects
     */
    private static final Map<String, Class<?>> PRIMITIVE_TYPES_AND_VOID;
    
    static 
    {
        Map<String, Class<?>> m = new LinkedHashMap<String, Class<?>>();
        m.put("void", void.class);
        m.put("boolean", boolean.class);
        m.put("byte", byte.class);
        m.put("char", char.class);
        m.put("short", short.class);
        m.put("int", int.class);
        m.put("long", long.class);
        m.put("float", float.class);
        m.put("double", double.class);
        PRIMITIVE_TYPES_AND_VOID = Collections.unmodifiableMap(m);
    }
    
    /**
     * Parse a type from the given string. The given string must either
     * be a fully qualified class name, or the name of a primitive type,
     * or <code>"void"</code>.
     * 
     * @param string The input string
     * @return The parsed class
     * @throws ReflectionException If no type could be parsed from the
     * given string
     */
    static Class<?> parseTypeUnchecked(String string)
    {
        Class<?> c = PRIMITIVE_TYPES_AND_VOID.get(string);
        if (c != null)
        {
            return c;
        }
        return Classes.forNameUnchecked(string);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Types()
    {
        // Private constructor to prevent instantiation
    }
}
