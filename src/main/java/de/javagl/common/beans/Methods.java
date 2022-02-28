/*
 * www.javagl.de - Common
 *
 * Copyright (c) 2012-2017 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility methods related to methods.
 */
class Methods
{
    // NOTE: These are extracted from https://github.com/javagl/Reflection
    // and changed to use IllegalArgumentException instead of
    // ReflectionException.
    
    /**
     * Delegates to {@link Method#invoke(Object, Object...)}.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link IllegalArgumentException}
     * 
     * @param method The method
     * @param object The object
     * @param arguments The arguments
     * @return The method call result
     * @throws IllegalArgumentException if the call did not succeed
     */
    static Object invokeUnchecked(
        Method method, Object object, Object ...arguments)
    {
        try
        {
            return method.invoke(object, arguments);
        } 
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException(e);
        } 
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * Delegates to {@link Method#invoke(Object, Object...)}.<br>
     * <br>
     * Any checked exception that may be thrown internally will silently 
     * be ignored, and <code>null</code> will be returned in this case. 
     * 
     * @param method The method
     * @param object The object
     * @param arguments The arguments
     * @return The method call result
     */
    static Object invokeOptional(
        Method method, Object object, Object ...arguments)
    {
        try
        {
            return method.invoke(object, arguments);
        } 
        catch (IllegalArgumentException e)
        {
            return null;
        } 
        catch (IllegalAccessException e)
        {
            return null;
        } 
        catch (InvocationTargetException e)
        {
            return null;
        }
    }
    
    /**
     * Delegates to {@link Class#getMethod(String, Class...)}.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link IllegalArgumentException}
     * 
     * @param type The type
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method
     * @throws IllegalArgumentException if the the method could not be obtained
     */
    static Method getMethodUnchecked(
        Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            return type.getMethod(methodName, parameterTypes);
        } 
        catch (SecurityException e)
        {
            throw new IllegalArgumentException(e);
        } 
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Delegates to {@link Class#getMethod(String, Class...)}.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param type The type
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method, or <code>null</code> if it could not be obtained
     */
    static Method getMethodOptional(
        Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            return type.getMethod(methodName, parameterTypes);
        } 
        catch (SecurityException e)
        {
            return null;
        } 
        catch (NoSuchMethodException e)
        {
            return null;
        }
    }
    

}
