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

import java.lang.reflect.Constructor;


/**
 * Utility methods related to classes.
 */
public class Classes
{
    /**
     * Returns the class for the given name.
     * The name must be a fully qualified class name.<br>
     * <br> 
     * This call is wrapping all possible checked exceptions and
     * initialization exceptions into a {@link ReflectionException}
     * 
     * @param className The class name
     * @return The class for the given name
     * @throws ReflectionException If the type for the given name
     * can not be found
     */
    public static Class<?> forNameUnchecked(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ExceptionInInitializerError e)
        {
            throw new ReflectionException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    /**
     * Returns the class for the given name.
     * The name must be a fully qualified class name.<br>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param className The class name
     * @return The class for the given name, or <code>null</code> if 
     * no such class could be found
     */
    public static Class<?> forNameOptional(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ExceptionInInitializerError e)
        {
            return null;
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
    }
    
    /**
     * Return a new instance of the type described by the given class name.
     * The name must be a fully qualified class name. The class must have a 
     * parameterless constructor.<br>
     * <br> 
     * This call is wrapping all possible checked exceptions and
     * initialization exceptions into a {@link ReflectionException}
     * 
     * @param className The fully qualified class name
     * @return A new instance of the class described by the given name
     * @throws ReflectionException If the type for the given name
     * can not be found or not be instantiated.
     */
    public static Object newInstanceUnchecked(String className)
    {
        try
        {
            Class<?> c = Class.forName(className);
            return newInstanceUnchecked(c);
        }
        catch (ExceptionInInitializerError e)
        {
            throw new ReflectionException(e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    /**
     * Return a new instance of the type described by the given class name.
     * The name must be a fully qualified class name. The class must have a 
     * parameterless constructor.<br>
     * <br> 
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param className The fully qualified class name
     * @return A new instance of the class described by the given name
     */
    public static Object newInstanceOptional(String className)
    {
        Class<?> c = forNameOptional(className);
        if (c == null)
        {
            return null;
        }
        return newInstanceOptional(c);
    }

    /**
     * Return a new instance of the given class. The class must have a 
     * parameterless constructor.<br>
     * <br> 
     * This call is wrapping all possible checked exceptions and
     * initialization exceptions into a {@link ReflectionException}
     * 
     * @param c The class
     * @return A new instance of the class
     * @throws ReflectionException If the instance could not be created,
     * for example, because it is an abstract class or interface, or
     * has no public default constructor
     */
    public static Object newInstanceUnchecked(Class<?> c)
    {
        try
        {
            Object instance = c.newInstance();
            return instance;
        }
        catch (InstantiationException e)
        {
            throw new ReflectionException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Return a new instance of the given class. The class must have a 
     * parameterless constructor.<br>
     * <br> 
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param c The class
     * @return A new instance of the class
     */
    public static Object newInstanceOptional(Class<?> c)
    {
        try
        {
            Object instance = c.newInstance();
            return instance;
        }
        catch (InstantiationException e)
        {
            return null;
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }
    
    
    /**
     * Return a new instance of the type described by the given class name.
     * The name must be a fully qualified class name, and the respective
     * class must have a parameterless constructor. If the constructor is
     * not accessible, it will be made accessible for this call. <br>
     * <br> 
     * This call is wrapping all possible checked exceptions and
     * initialization exceptions into a {@link ReflectionException}
     * 
     * @param className The fully qualified class name
     * @return A new instance of the class described by the given name
     * @throws ReflectionException If the type for the given name
     * can not be found or not be instantiated.
     */
    public static Object newInstanceNonAccessibleUnchecked(String className)
    {
        Class<?> c = forNameUnchecked(className);
        return newInstanceNonAccessibleUnchecked(c);
    }

    /**
     * Return a new instance of the type described by the given class name.
     * The name must be a fully qualified class name, and the respective
     * class must have a parameterless constructor. If the constructor is
     * not accessible, it will be made accessible for this call. <br>
     * <br> 
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param className The fully qualified class name
     * @return A new instance of the class described by the given name
     */
    public static Object newInstanceNonAccessibleOptional(String className)
    {
        Class<?> c = forNameOptional(className);
        if (c == null)
        {
            return null;
        }
        return newInstanceNonAccessibleOptional(c);
    }
    
    /**
     * Return a new instance of the given class. The class must have a 
     * parameterless constructor. If the constructor is not accessible, 
     * it will be made accessible for this call. <br>
     * <br> 
     * This call is wrapping all possible checked exceptions and
     * initialization exceptions into a {@link ReflectionException}
     * 
     * @param c The class
     * @return A new instance of the class
     * @throws ReflectionException If the instance could not be created,
     * for example, because it is an abstract class or interface, or
     * has no parameterless constructor
     */
    public static Object newInstanceNonAccessibleUnchecked(Class<?> c)
    {
        Constructor<?> constructor = 
            Constructors.getDeclaredConstructorUnchecked(c);
        return Constructors.newInstanceNonAccessibleUnchecked(constructor);
    }
    
    /**
     * Return a new instance of the given class. The class must have a 
     * parameterless constructor. If the constructor is not accessible, 
     * it will be made accessible for this call. <br>
     * <br> 
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param c The class
     * @return A new instance of the class
     */
    public static Object newInstanceNonAccessibleOptional(Class<?> c)
    {
        Constructor<?> constructor = 
            Constructors.getDeclaredConstructorUnchecked(c);
        if (constructor == null)
        {
            return null;
        }
        return Constructors.newInstanceNonAccessibleOptional(constructor);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Classes()
    {
        // Private constructor to prevent instantiation
    }
}
