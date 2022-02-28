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

import de.javagl.reflection.InvokableParser.InvokableInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Utility methods related to methods. 
 */
public class Methods
{
    /**
     * Parse the method from the given string. The given string must be
     * the string that is obtained from a method by calling 
     * {@link Method#toString()} or {@link Method#toGenericString()}.
     * 
     * @param fullMethodString The full method string
     * @return The method
     * @throws ReflectionException If the method can not be parsed
     * for any reason. Either because the declaring class or any parameter
     * class can not be found, or because the method is not found, or
     * because the input string is otherwise invalid.
     */
    public static Method parseMethodUnchecked(String fullMethodString)
    {
        InvokableInfo invokableInfo = InvokableParser.parse(fullMethodString);
        
        // Fetch the fully qualified method name, which is of the form
        // com.example.ClassName.methodName
        // and extract the ClassName and methodName
        String classAndMethodName = invokableInfo.getFullyQualifiedName(); 
        int dotIndex = classAndMethodName.lastIndexOf('.');
        if (dotIndex == -1)
        {
            throw new ReflectionException(
                "No method in input string: " + fullMethodString);
        }
        String className = classAndMethodName.substring(0, dotIndex);
        String methodName = classAndMethodName.substring(dotIndex+1);
        
        // Try to find the declaring class
        Class<?> declaringClass = Types.parseTypeUnchecked(className);
        
        // Finally, try to find the method
        Class<?> parameterTypes[] = invokableInfo.getParameterTypes(); 
        Method result = getDeclaredMethodUnchecked(
            declaringClass, methodName, parameterTypes);
        return result;
    }
    
    
    
    
    
    
    //=========================================================================
    // Single method
    // general and declared
    // fully qualified
    // unchecked and optional
    
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
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method
     * @throws ReflectionException if the the method could not be obtained
     */
    public static Method getMethodUnchecked(
        Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            return type.getMethod(methodName, parameterTypes);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
        catch (NoSuchMethodException e)
        {
            throw new ReflectionException(e);
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
    public static Method getMethodOptional(
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
    

    /**
     * Delegates to {@link Class#getDeclaredMethod(String, Class...)}.<br>  
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>excluding</i> methods from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method
     * @throws ReflectionException if the method could not be obtained
     */
    public static Method getDeclaredMethodUnchecked(
        Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            return type.getDeclaredMethod(methodName, parameterTypes);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
        catch (NoSuchMethodException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Delegates to {@link Class#getDeclaredMethod(String, Class...)}.<br>  
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>excluding</i> methods from supertypes</li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param type The type
     * @param methodName The method name
     * @param parameterTypes The parameter types
     * @return The method, or <code>null</code> if it could not be obtained
     */
    public static Method getDeclaredMethodOptional(
        Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            return type.getDeclaredMethod(methodName, parameterTypes);
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

    
    //=========================================================================
    // All methods
    // general and declared
    // unchecked and optional
    

    /**
     * Delegates to {@link Class#getMethods()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>excluding</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     *   <li>
     *     <i>excluding</i> the <b>static</b> methods of super<b>interfaces</b>
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The methods
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Method> getMethodsUnchecked(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getMethods()));
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }
    
    /**
     * Delegates to {@link Class#getMethods()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>excluding</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     *   <li>
     *     <i>excluding</i> the <b>static</b> methods of super<b>interfaces</b>
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The method
     */
    public static List<Method> getMethodsOptional(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getMethods()));
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }
    
    
    /**
     * Delegates to {@link Class#getDeclaredMethods()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>excluding</i> methods from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The method
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Method> getDeclaredMethodsUnchecked(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getDeclaredMethods()));
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }

    /**
     * Delegates to {@link Class#getDeclaredMethods()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>excluding</i> methods from supertypes</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The method
     */
    public static List<Method> getDeclaredMethodsOptional(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getDeclaredMethods()));
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }
    

    //=========================================================================
    // All methods (deep search)
    // general and declared
    // unchecked and optional

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>including</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The list of all methods 
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Method> getAllMethodsUnchecked(
        Class<?> type)
    {
        try
        {
            return getAllMethods(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>including</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The list of all methods 
     */
    public static List<Method> getAllMethodsOptional(
        Class<?> type)
    {
        try
        {
            return getAllMethods(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }
    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.
     * 
     * @param type The type
     * @return The list of inherited methods
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Method> getAllMethods(Class<?> type)
    {
        Set<Method> methods = new LinkedHashSet<Method>(); 
        methods.addAll(Arrays.asList(type.getMethods()));

        Class<?> superclass = type.getSuperclass();
        if (superclass != null)
        {
            methods.addAll(getAllMethods(superclass)); // Recursion done here!
        }
        if (type.isInterface())
        {
            methods.addAll(Arrays.asList(Object.class.getMethods()));
        }
        for (Class<?> i : type.getInterfaces())
        {
            methods.addAll(Arrays.asList(i.getMethods()));
        }
        return Collections.unmodifiableList(
            new ArrayList<Method>(methods));
    }
    
    
    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>including</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The list of all declared methods 
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Method> getAllDeclaredMethodsUnchecked(
        Class<?> type)
    {
        try
        {
            return getAllDeclaredMethods(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.<br>
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li><i>including</i> methods from supertypes</li>
     *   <li>
     *     <i>including</i> the methods of supertypes that have been 
     *     overridden by the given type
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The list of all declared methods 
     */
    public static List<Method> getAllDeclaredMethodsOptional(
        Class<?> type)
    {
        try
        {
            return getAllDeclaredMethods(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }
    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type or any 
     * of its supertypes or its implemented interfaces.
     * 
     * @param type The type
     * @return The list of inherited methods
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Method> getAllDeclaredMethods(Class<?> type)
    {
        Set<Method> methods = new LinkedHashSet<Method>(); 
        methods.addAll(Arrays.asList(type.getDeclaredMethods()));

        Class<?> superclass = type.getSuperclass();
        if (superclass != null)
        {
            methods.addAll(getAllDeclaredMethods(superclass)); // Recursion!
        }
        if (type.isInterface())
        {
            methods.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
        }
        for (Class<?> i : type.getInterfaces())
        {
            methods.addAll(Arrays.asList(i.getDeclaredMethods()));
        }
        return Collections.unmodifiableList(
            new ArrayList<Method>(methods));
    }
    
    
    
    
    
    
    //=========================================================================
    // All own methods
    // general and declared
    // unchecked and optional

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type, except for
     * those that are inherited from any supertype or one of its implemented
     * interfaces.<br> 
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>instance</b> methods</li>
     *   <li>
     *     <i>excluding</i> methods from supertypes (even if they are
     *     overridden by the given type)
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The methods
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Method> getOwnMethodsUnchecked(Class<?> type)
    {
        try
        {
            return getOwnMethods(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type, except for
     * those that are inherited from any supertype or one of its implemented
     * interfaces.<br> 
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li><b>public</b> methods</li>
     *   <li><b>instance</b> methods</li>
     *   <li>
     *     <i>excluding</i> methods from supertypes (even if they are
     *     overridden by the given type)
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The methods
     */
    public static List<Method> getOwnMethodsOptional(Class<?> type)
    {
        try
        {
            return getOwnMethods(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getMethods()} on the given type, except for
     * those that are inherited from any supertype or one of its implemented
     * interfaces. 
     * 
     * @param type The type
     * @return The methods
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Method> getOwnMethods(Class<?> type)
    {
        Set<Method> toRemove = new LinkedHashSet<Method>();
        Class<?> superclass = type.getSuperclass();
        if (superclass != null)
        {
            toRemove.addAll(Arrays.asList(superclass.getMethods()));
        }
        if (type.isInterface())
        {
            toRemove.addAll(Arrays.asList(Object.class.getMethods()));
        }
        for (Class<?> i : type.getInterfaces())
        {
            toRemove.addAll(Arrays.asList(i.getMethods()));
        }
        List<Method> result = new ArrayList<Method>();
        for (Method method : type.getMethods())
        {
            if (!containsEquivalent(toRemove, method))
            {
                result.add(method);
            }
        }
        return Collections.unmodifiableList(result);
    }

    
    
    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type, 
     * except for those that are inherited from any supertype or one of its 
     * implemented interfaces.<br> 
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li>
     *     <i>excluding</i> methods from supertypes (even if they are
     *     overridden by the given type)
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The methods
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Method> getOwnDeclaredMethodsUnchecked(Class<?> type)
    {
        try
        {
            return getOwnDeclaredMethods(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type, 
     * except for those that are inherited from any supertype or one of its 
     * implemented interfaces.<br> 
     * <br>
     * This call covers the following methods:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b> and 
     *     <b>private</b> methods
     *   </li>
     *   <li><b>static</b> and <b>instance</b> methods</li>
     *   <li>
     *     <i>excluding</i> methods from supertypes (even if they are
     *     overridden by the given type)
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The methods
     */
    public static List<Method> getOwnDeclaredMethodsOptional(Class<?> type)
    {
        try
        {
            return getOwnDeclaredMethods(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }

    /**
     * Returns an unmodifiable list containing all methods of that are returned
     * by a call to {@link Class#getDeclaredMethods()} on the given type, except 
     * for those that are inherited from any supertype or one of its implemented
     * interfaces. 
     * 
     * @param type The type
     * @return The methods
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Method> getOwnDeclaredMethods(Class<?> type)
    {
        Set<Method> toRemove = new LinkedHashSet<Method>();
        Class<?> superclass = type.getSuperclass();
        if (superclass != null)
        {
            toRemove.addAll(Arrays.asList(superclass.getDeclaredMethods()));
        }
        if (type.isInterface())
        {
            toRemove.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
        }
        for (Class<?> i : type.getInterfaces())
        {
            toRemove.addAll(Arrays.asList(i.getDeclaredMethods()));
        }
        List<Method> result = new ArrayList<Method>();
        for (Method method : type.getDeclaredMethods())
        {
            if (!containsEquivalent(toRemove, method))
            {
                result.add(method);
            }
        }
        return Collections.unmodifiableList(result);
    }
    
    
    
    

    /**
     * Returns whether the given sequence contains a method that 
     * {@link #areEquivalent(Method, Method) is equivalent} to 
     * the given method.
     * 
     * @param methods The methods
     * @param method The method
     * @return Whether one of the given methods is equivalent to the
     * given method
     */
    private static boolean containsEquivalent(
        Iterable<? extends Method> methods, Method method)
    {
        for (Method other : methods)
        {
            if (areEquivalent(method, other))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the given methods are equivalent. That is, whether
     * they have equal names and parameter types.
     * 
     * @param method0 The first method
     * @param method1 The second method
     * @return Whether the methods are equivalent
     */
    private static boolean areEquivalent(Method method0, Method method1)
    {
        String name0 = method0.getName();
        String name1 = method1.getName();
        if (!name0.equals(name1))
        {
            return false;
        }
        Class<?> parameterTypes0[] = method0.getParameterTypes();
        Class<?> parameterTypes1[] = method1.getParameterTypes();
        return Arrays.equals(parameterTypes0, parameterTypes1);
    }
    
    
    //=========================================================================

    
    /**
     * Returns an unmodifiable list containing all methods of the given type 
     * that match the given predicates.<br>
     * <br>
     * This is equivalent to filtering the list of methods that is returned
     * by {@link #getAllDeclaredMethodsOptional(Class)} with the given 
     * predicates.<br>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @param predicates The predicates
     * @return The methods
     */
    @SafeVarargs
    public static List<Method> getAllOptional(
        Class<?> type, Predicate<? super Method> ... predicates)
    {
        return Lists.filter(getAllDeclaredMethodsOptional(type), predicates);
    }

    /**
     * Returns an unmodifiable list containing all methods of the given type 
     * that match the given predicates.<br>
     * <br>
     * This is equivalent to filtering the list of methods that is returned
     * by {@link #getAllDeclaredMethodsUnchecked(Class)} with the given 
     * predicates.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param predicates The predicates
     * @return The methods
     * @throws ReflectionException if the call did not succeed
     */
    @SafeVarargs
    public static List<Method> getAllUnchecked(
        Class<?> type, Predicate<? super Method> ... predicates)
    {
        return Lists.filter(getAllDeclaredMethodsUnchecked(type), predicates);
    }
    
    //=========================================================================
    // Invocation
    
    /**
     * Delegates to {@link Method#invoke(Object, Object...)}.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param method The method
     * @param object The object
     * @param arguments The arguments
     * @return The method call result
     * @throws ReflectionException if the call did not succeed
     */
    public static Object invokeUnchecked(
        Method method, Object object, Object ...arguments)
    {
        try
        {
            return method.invoke(object, arguments);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (InvocationTargetException e)
        {
            throw new ReflectionException(e);
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
    public static Object invokeOptional(
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
     * Delegates to {@link Method#invoke(Object, Object...)}. <br>
     * <br>
     * If the given method is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param method The method
     * @param object The object
     * @param arguments The arguments
     * @return The method call result
     * @throws ReflectionException if the call did not succeed
     */
    public static Object invokeNonAccessibleUnchecked(
        Method method, Object object, Object ...arguments)
    {
        boolean wasAccessible = method.isAccessible();
        try
        {
            method.setAccessible(true);
            Object result = method.invoke(object, arguments);
            return result;
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (InvocationTargetException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            method.setAccessible(wasAccessible);
        }
    }

    /**
     * Delegates to {@link Method#invoke(Object, Object...)}. <br>
     * <br>
     * If the given method is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Any checked exception that may be thrown internally will silently 
     * be ignored, and <code>null</code> will be returned in this case. 
     * 
     * @param method The method
     * @param object The object
     * @param arguments The arguments
     * @return The method call result
     */
    public static Object invokeNonAccessibleOptional(
        Method method, Object object, Object ...arguments)
    {
        boolean wasAccessible = method.isAccessible();
        try
        {
            method.setAccessible(true);
            Object result = method.invoke(object, arguments);
            return result;
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
        catch (SecurityException e)
        {
            return null;
        }
        finally
        {
            method.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Methods()
    {
        // Private constructor to prevent instantiation
    }

    
}
