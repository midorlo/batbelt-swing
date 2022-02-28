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

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * Utility methods related to fields
 */
public class Fields
{
    /**
     * Calls {@link Class#getField(String)}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param fieldName The field name
     * @return The field
     * @throws ReflectionException if the call did not succeed
     */
    public static Field getFieldUnchecked(
        Class<?> type, String fieldName)
    {
        try
        {
            return type.getField(fieldName);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
        catch (NoSuchFieldException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Calls {@link Class#getField(String)}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param type The type
     * @param fieldName The field name
     * @return The field, or <code>null</code> if it could not be obtained
     */
    public static Field getFieldOptional(
        Class<?> type, String fieldName)
    {
        try
        {
            return type.getField(fieldName);
        } 
        catch (SecurityException e)
        {
            return null;
        } 
        catch (NoSuchFieldException e)
        {
            return null;
        }
    }
    
    /**
     * Calls {@link Class#getDeclaredField(String)}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>  
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param fieldName The field name
     * @return The field
     * @throws ReflectionException if the call did not succeed
     */
    public static Field getDeclaredFieldUnchecked(
        Class<?> type, String fieldName)
    {
        try
        {
            return type.getDeclaredField(fieldName);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
        catch (NoSuchFieldException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Calls {@link Class#getDeclaredField(String)}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param type The type
     * @param fieldName The field name
     * @return The field, if it is declared in the given type,
     * or <code>null</code> otherwise.
     */
    public static Field getDeclaredFieldOptional(
        Class<?> type, String fieldName)
    {
        try
        {
            return type.getDeclaredField(fieldName);
        } 
        catch (SecurityException e)
        {
            return null;
        } 
        catch (NoSuchFieldException e)
        {
            return null;
        }
    }

    
    
    /**
     * Returns an unmodifiable List containing all fields that result 
     * from a call to {@link Class#getFields()}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The fields
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Field> getFieldsUnchecked(Class<?> type)
    {
        try
        {
            return Arrays.asList(type.getFields());
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }
    
    /**
     * Returns an unmodifiable List containing all fields that result 
     * from a call to {@link Class#getFields()}. <br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The field
     */
    public static List<Field> getFieldsOptional(Class<?> type)
    {
        try
        {
            return Arrays.asList(type.getFields());
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }
    
    
    /**
     * Returns an unmodifiable List containing all fields that result 
     * from a call to {@link Class#getDeclaredFields()}<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The field
     * @throws ReflectionException if the call did not succeed
     */
    public static List<Field> getDeclaredFieldsUnchecked(Class<?> type)
    {
        try
        {
            return Arrays.asList(type.getDeclaredFields());
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        } 
    }

    /**
     * Returns an unmodifiable List containing all fields that result 
     * from a call to {@link Class#getDeclaredFields()}.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The field
     */
    public static List<Field> getDeclaredFieldsOptional(Class<?> type)
    {
        try
        {
            return Arrays.asList(type.getDeclaredFields());
        } 
        catch (SecurityException e)
        {
            return Collections.emptyList();
        } 
    }

    
    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getDeclaredFields()} on the given type or any 
     * of its supertypes.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The list of all declared fields 
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Field> getAllDeclaredFieldsUnchecked(Class<?> type)
    {
        try
        {
            return getAllDeclaredFields(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    
    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getDeclaredFields()} on the given type or any 
     * of its supertypes.<br>
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li>
     *     <b>public</b>, <b>protected</b>, <b>default</b>, 
     *     and <b>private</b> fields
     *   </li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>including</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The list of all declared fields 
     */
    public static List<Field> getAllDeclaredFieldsOptional(Class<?> type)
    {
        try
        {
            return getAllDeclaredFields(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }
    
    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getDeclaredFields()} on the given type or any 
     * of its supertypes.
     * 
     * @param type The type
     * @return The list of inherited fields
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Field> getAllDeclaredFields(Class<?> type)
    {
        Set<Field> fields = new LinkedHashSet<Field>(); 
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        Class<?> currentSuperclass = type.getSuperclass();
        while (currentSuperclass != null)
        {
            fields.addAll(getAllDeclaredFields(currentSuperclass));
            currentSuperclass = currentSuperclass.getSuperclass();
        }
        for (Class<?> i : type.getInterfaces())
        {
            fields.addAll(getAllDeclaredFields(i));
        }
        return Collections.unmodifiableList(new ArrayList<Field>(fields));
    }
    
    
    
    
    
    
    //=========================================================================
    // All own fields
    // general 
    // unchecked and optional

    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getFields()} on the given type, except for
     * those that are inherited from any supertype.<br> 
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @return The fields
     * @throws ReflectionException if the call did not succeed.
     */
    public static List<Field> getOwnFieldsUnchecked(Class<?> type)
    {
        try
        {
            return getOwnFields(type);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }

    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getFields()} on the given type, except for
     * those that are inherited from any supertype.<br> 
     * <br>
     * This call covers the following fields:
     * <ul>
     *   <li><b>public</b> fields</li>
     *   <li><b>static</b> and <b>instance</b> fields</li>
     *   <li><i>excluding</i> fields from supertypes</li>
     * </ul>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @return The fields
     */
    public static List<Field> getOwnFieldsOptional(Class<?> type)
    {
        try
        {
            return getOwnFields(type);
        }
        catch (SecurityException e)
        {
            return Collections.emptyList();
        }
    }
    
    /**
     * Returns an unmodifiable list containing all fields of that are returned
     * by a call to {@link Class#getFields()} on the given type, except for
     * those that are inherited from any supertype.<br> 
     * <br>
     * @param type The type
     * @return The fields
     * @throws SecurityException If the delegate call throws it 
     */
    private static List<Field> getOwnFields(Class<?> type)
    {
        Set<Field> toRemove = new LinkedHashSet<Field>();
        Class<?> currentSuperclass = type.getSuperclass(); 
        while (currentSuperclass != null)
        {
            toRemove.addAll(Arrays.asList(currentSuperclass.getFields()));
            currentSuperclass = currentSuperclass.getSuperclass();
        }
        for (Class<?> i : type.getInterfaces())
        {
            toRemove.addAll(Arrays.asList(i.getFields()));
        }
        Set<Field> result = 
            new LinkedHashSet<Field>(Arrays.asList(type.getFields()));
        result.removeAll(toRemove);
        return Collections.unmodifiableList(new ArrayList<Field>(result));
    }
    
    
    //=========================================================================
    
    /**
     * Returns an unmodifiable list containing all fields of the given type 
     * that match the given predicates.<br>
     * <br>
     * This is equivalent to filtering the list of fields that is returned
     * by {@link #getAllDeclaredFieldsOptional(Class)} with the given 
     * predicates.<br>
     * <br>
     * Returns an empty list if the underlying call did not succeed.
     * 
     * @param type The type
     * @param predicates The predicates
     * @return The fields
     */
    @SafeVarargs
    public static List<Field> getAllOptional(
        Class<?> type, Predicate<? super Field> ... predicates)
    {
        return Lists.filter(getAllDeclaredFieldsOptional(type), predicates);
    }

    /**
     * Returns an unmodifiable list containing all fields of the given type 
     * that match the given predicates.<br>
     * <br>
     * This is equivalent to filtering the list of fields that is returned
     * by {@link #getAllDeclaredFieldsUnchecked(Class)} with the given 
     * predicates.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param type The type
     * @param predicates The predicates
     * @return The fields
     * @throws ReflectionException if the call did not succeed
     */
    @SafeVarargs
    public static List<Field> getAllUnchecked(
        Class<?> type, Predicate<? super Field> ... predicates)
    {
        return Lists.filter(getAllDeclaredFieldsUnchecked(type), predicates);
    }
    
    //=========================================================================
    // Access: Set
    
    /**
     * Set the value for the given field in the given object.<br>
     * <br>
     * Note that this call may also be used for <i>primitive</i> values,
     * as the (boxed) parameter will automatically be converted to the
     * primitive type if necessary.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @param value The value
     * @throws ReflectionException if the call did not succeed
     */
    public void setUnchecked(Field field, Object object, Object value)
    {
        try
        {
            field.set(object, value);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    /**
     * Set the value for the given field in the given object.<br>
     * <br>
     * Note that this call may also be used for <i>primitive</i> values,
     * as the (boxed) parameter will automatically be converted to the
     * primitive type if necessary.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @param value The value
     * @throws ReflectionException if the call did not succeed
     */
    public static void setNonAccessibleUnchecked(
        Field field, Object object, Object value)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            field.set(object,  value);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }

    /**
     * Set the value for the given field in the given object.<br>
     * <br>
     * Note that this call may also be used for <i>primitive</i> values,
     * as the (boxed) parameter will automatically be converted to the
     * primitive type if necessary.<br>
     * <br>
     * This method does nothing if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param value The value
     */
    public void setOptional(Field field, Object object, Object value)
    {
        try
        {
            field.set(object, value);
        } 
        catch (IllegalArgumentException e)
        {
            // Ignored
        } 
        catch (IllegalAccessException e)
        {
            // Ignored
        }
        catch (SecurityException e)
        {
            // Ignored
        }
    }
    
    /**
     * Set the value for the given field in the given object.<br>
     * <br>
     * Note that this call may also be used for <i>primitive</i> values,
     * as the (boxed) parameter will automatically be converted to the
     * primitive type if necessary.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This method does nothing if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param value The value
     */
    public static void setNonAccessibleOptional(
        Field field, Object object, Object value)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            field.set(object,  value);
        } 
        catch (IllegalArgumentException e)
        {
            // Ignored
        } 
        catch (IllegalAccessException e)
        {
            // Ignored
        } 
        catch (SecurityException e)
        {
            // Ignored
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }

    //=========================================================================
    // Access: Get Object
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public Object getUnchecked(Field field, Object object)
    {
        try
        {
            return field.get(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static Object getNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.get(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @return The value
     */
    public Object getOptional(Field field, Object object)
    {
        return getOptional(field, object, null);
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns <code>null</code> if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @return The value
     */
    public static Object getNonAccessibleOptional(
        Field field, Object object)
    {
        return getNonAccessibleOptional(field, object, null);
    }
    
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public Object getOptional(Field field, Object object, Object defaultValue)
    {
        try
        {
            return field.get(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static Object getNonAccessibleOptional(
        Field field, Object object, Object defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.get(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    
    
    //=========================================================================
    // Access: Get boolean
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public boolean getBooleanUnchecked(Field field, Object object)
    {
        try
        {
            return field.getBoolean(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static boolean getBooleanNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getBoolean(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public boolean getBooleanOptional(
        Field field, Object object, boolean defaultValue)
    {
        try
        {
            return field.getBoolean(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static boolean getBooleanNonAccessibleOptional(
        Field field, Object object, boolean defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getBoolean(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    
    //=========================================================================
    // Access: Get byte
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public byte getByteUnchecked(Field field, Object object)
    {
        try
        {
            return field.getByte(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static byte getByteNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getByte(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public byte getByteOptional(
        Field field, Object object, byte defaultValue)
    {
        try
        {
            return field.getByte(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static byte getByteNonAccessibleOptional(
        Field field, Object object, byte defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getByte(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    
    //=========================================================================
    // Access: Get short
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public short getShortUnchecked(Field field, Object object)
    {
        try
        {
            return field.getShort(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static short getShortNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getShort(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public short getShortOptional(
        Field field, Object object, short defaultValue)
    {
        try
        {
            return field.getShort(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static short getShortNonAccessibleOptional(
        Field field, Object object, short defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getShort(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    //=========================================================================
    // Access: Get char
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public char getCharUnchecked(Field field, Object object)
    {
        try
        {
            return field.getChar(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static char getCharNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getChar(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public char getCharOptional(
        Field field, Object object, char defaultValue)
    {
        try
        {
            return field.getChar(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static char getCharNonAccessibleOptional(
        Field field, Object object, char defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getChar(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    //=========================================================================
    // Access: Get int
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public int getIntUnchecked(Field field, Object object)
    {
        try
        {
            return field.getInt(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static int getIntNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getInt(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public int getIntOptional(
        Field field, Object object, int defaultValue)
    {
        try
        {
            return field.getInt(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static int getIntNonAccessibleOptional(
        Field field, Object object, int defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getInt(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    //=========================================================================
    // Access: Get long
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public long getLongUnchecked(Field field, Object object)
    {
        try
        {
            return field.getLong(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static long getLongNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getLong(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public long getLongOptional(
        Field field, Object object, long defaultValue)
    {
        try
        {
            return field.getLong(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static long getLongNonAccessibleOptional(
        Field field, Object object, long defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getLong(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    //=========================================================================
    // Access: Get float
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public float getFloatUnchecked(Field field, Object object)
    {
        try
        {
            return field.getFloat(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static float getFloatNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getFloat(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public float getFloatOptional(
        Field field, Object object, float defaultValue)
    {
        try
        {
            return field.getFloat(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static float getFloatNonAccessibleOptional(
        Field field, Object object, float defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getFloat(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }

    //=========================================================================
    // Access: Get double
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public double getDoubleUnchecked(Field field, Object object)
    {
        try
        {
            return field.getDouble(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        }
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * This call is wrapping all possible checked exceptions and
     * SecurityExceptions into a {@link ReflectionException}
     * 
     * @param field The field
     * @param object The object
     * @return The value
     * @throws ReflectionException if the call did not succeed
     */
    public static double getDoubleNonAccessibleUnchecked(
        Field field, Object object)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getDouble(object);
        } 
        catch (IllegalArgumentException e)
        {
            throw new ReflectionException(e);
        } 
        catch (IllegalAccessException e)
        {
            throw new ReflectionException(e);
        } 
        catch (SecurityException e)
        {
            throw new ReflectionException(e);
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public double getDoubleOptional(
        Field field, Object object, double defaultValue)
    {
        try
        {
            return field.getDouble(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        }
        catch (SecurityException e)
        {
            return defaultValue;
        }
    }
    
    
    /**
     * Get the value of the given field in the given object.<br>
     * <br>
     * If the given field is not accessible, it will be set to be 
     * accessible and its accessibility status will be restored after
     * the call.<br>
     * <br>
     * Returns the default value if the underlying call did not succeed.
     * 
     * @param field The field
     * @param object The object
     * @param defaultValue The default value
     * @return The value
     */
    public static double getDoubleNonAccessibleOptional(
        Field field, Object object, double defaultValue)
    {
        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            return field.getDouble(object);
        } 
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        } 
        catch (IllegalAccessException e)
        {
            return defaultValue;
        } 
        catch (SecurityException e)
        {
            return defaultValue;
        }
        finally
        {
            field.setAccessible(wasAccessible);
        }
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Fields()
    {
        // Private constructor to prevent instantiation
    }

}
