package de.javagl.reflection;

import de.javagl.reflection.InvokableParser.InvokableInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Utility methods related to constructors. <br>
 * <br>
 */
public class Constructors
{
    /**
     * Parse the constructor from the given string. The given string must be
     * the string that is obtained from a constructor by calling 
     * {@link Constructor#toString()} or {@link Constructor#toGenericString()}.
     * 
     * @param fullConstructorString The full constructor string
     * @return The constructor
     * @throws ReflectionException If the constructor can not be parsed
     * for any reason. Either because the declaring class or any parameter
     * class can not be found, or because the constructor is not found, or
     * because the input string is otherwise invalid.
     */
    public static Constructor<?> parseConstructorUnchecked(
        String fullConstructorString)
    {
        InvokableInfo invokableInfo = 
            InvokableParser.parse(fullConstructorString);
        
        // Fetch the fully qualified constructor name, which is of the form
        // com.domain.ClassName
        String className = invokableInfo.getFullyQualifiedName(); 
        
        // Try to find the declaring class
        Class<?> declaringClass = Types.parseTypeUnchecked(className);
        
        // Finally, try to find the constructor
        Class<?> parameterTypes[] = invokableInfo.getParameterTypes(); 
        Constructor<?> result = getDeclaredConstructorUnchecked(
            declaringClass, parameterTypes);
        return result;
    }
    

    /**
     * Delegates to {@link Constructor#newInstance(Object...)}.<br> 
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param <T> The type of the constructor
     * @param constructor The constructor
     * @param arguments The arguments
     * @return The new instance
     * @throws ReflectionException if the call did not succeed
     */
    public static <T> T newInstanceUnchecked(
        Constructor<T> constructor, Object ... arguments)
    {
        try
        {
            T t = constructor.newInstance(arguments);
            return t;
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (InstantiationException e)
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
    }
    
    /**
     * Delegates to {@link Constructor#newInstance(Object...)}.<br> 
     * <br>
     * Returns <code>null</code> if the call did not succeed.
     * 
     * @param <T> The type of the constructor
     * @param constructor The constructor
     * @param arguments The arguments
     * @return The new instance
     * @throws ReflectionException if the call did not succeed
     */
    public static <T> T newInstanceOptional(
        Constructor<T> constructor, Object ... arguments)
    {
        try
        {
            T t = constructor.newInstance(arguments);
            return t;
        } 
        catch (IllegalArgumentException e)
        {
            return null;
        } 
        catch (InstantiationException e)
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
    }
    
    /**
     * Delegates to {@link Constructor#newInstance(Object...)}.<br>
     * <br>
     * If the constructor is not accessible, then it will be made 
     * accessible for this call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param <T> The type of the constructor
     * @param constructor The constructor
     * @param arguments The arguments
     * @return The new instance
     * @throws ReflectionException if the call did not succeed
     */
    public static <T> T newInstanceNonAccessibleUnchecked(
        Constructor<T> constructor, Object ... arguments)
    {
        boolean wasAccessible = constructor.isAccessible();
        try
        {
            constructor.setAccessible(true);
            T t = constructor.newInstance(arguments);
            return t;
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (InstantiationException e)
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
            constructor.setAccessible(wasAccessible);
        }
    }
    
    
    
    /**
     * Delegates to {@link Constructor#newInstance(Object...)}.<br>
     * <br>
     * If the constructor is not accessible, then it will be made 
     * accessible for this call.<br>
     * <br>
     * Returns <code>null</code> if the call did not succeed.
     * 
     * @param <T> The type of the constructor
     * @param constructor The constructor
     * @param arguments The arguments
     * @return The new instance
     */
    public static <T> T newInstanceNonAccessibleOptional(
        Constructor<T> constructor, Object ... arguments)
    {
        boolean wasAccessible = constructor.isAccessible();
        try
        {
            constructor.setAccessible(true);
            T t = constructor.newInstance(arguments);
            return t;
        } 
        catch (IllegalArgumentException e)
        {
            return null;
        } 
        catch (InstantiationException e)
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
            constructor.setAccessible(wasAccessible);
        }
    }
    
    
    /**
     * Calls {@link Class#getConstructor(Class...)}.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li><b>public</b> constructors</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param <T> The type 
     * @param type The type
     * @param parameterTypes The parameter types
     * @return The constructor
     * @throws ReflectionException if the call did not succeed
     */
    public static <T> Constructor<T> getConstructorUnchecked(
        Class<T> type, Class<?>... parameterTypes)
    {
        try
        {
            return type.getConstructor(parameterTypes);
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
     * Calls {@link Class#getConstructor(Class...)}.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li><b>public</b> constructors</li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the call did not succeed.
     * 
     * @param <T> The type 
     * @param type The type
     * @param parameterTypes The parameter types
     * @return The constructor or <code>null</code>
     */
    public static <T> Constructor<T> getConstructorOptional(
        Class<T> type, Class<?>... parameterTypes)
    {
        try
        {
            return type.getConstructor(parameterTypes);
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
     * Delegates to {@link Class#getConstructors()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li><b>public</b> constructors</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The constructors
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Constructor<?>> getConstructorsUnchecked(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getConstructors()));
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }

    /**
     * Delegates to {@link Class#getConstructors()}, and returns the result
     * as an unmodifiable list.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li><b>public</b> constructors</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The constructors
     */
    public static List<Constructor<?>> getConstructorsOptional(Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getConstructors()));
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }
    

    /**
     * Calls {@link Class#getDeclaredConstructor(Class...)}.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> constructors
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param <T> The type 
     * @param type The type
     * @param parameterTypes The parameter types
     * @return The constructor
     * @throws ReflectionException if the call did not succeed
     */
    public static <T> Constructor<T> getDeclaredConstructorUnchecked(
        Class<T> type, Class<?>... parameterTypes)
    {
        try
        {
            return type.getDeclaredConstructor(parameterTypes);
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
     * Calls {@link Class#getDeclaredConstructor(Class...)}.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> constructors
     *   </li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the call did not succeed.
     * 
     * @param <T> The type 
     * @param type The type
     * @param parameterTypes The parameter types
     * @return The constructor or <code>null</code>
     */
    public static <T> Constructor<T> getDeclaredConstructorOptional(
        Class<T> type, Class<?>... parameterTypes)
    {
        try
        {
            return type.getDeclaredConstructor(parameterTypes);
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
     * Delegates to {@link Class#getDeclaredConstructors()}, and returns the 
     * result as an unmodifiable list.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> constructors
     *   </li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The constructors
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Constructor<?>> getDeclaredConstructorsUnchecked(
        Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getDeclaredConstructors()));
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }

    /**
     * Delegates to {@link Class#getDeclaredConstructors()}, and returns the 
     * result as an unmodifiable list.<br>
     * <br>
     * This call covers the following constructors:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> constructors
     *   </li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The constructors
     */
    public static List<Constructor<?>> getDeclaredConstructorsOptional(
        Class<?> type)
    {
        try
        {
            return Collections.unmodifiableList(
                Arrays.asList(type.getDeclaredConstructors()));
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Constructors()
    {
        // Private constructor to prevent instantiation
    }
    
}
