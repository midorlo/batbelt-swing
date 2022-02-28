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

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Utility methods related to property change listeners
 */
public class PropertyChangeUtils
{
    /**
     * Returns whether the given class seems to maintain PropertyChangeListener
     * instances. That is, whether the given class has the public instance 
     * methods 
     * <code>addPropertyChangeListener(PropertyChangeListener)</code>
     * and
     * <code>removePropertyChangeListener(PropertyChangeListener)</code>
     * 
     * @param c The class
     * @return Whether the given class maintains PropertyChangeListeners
     * @throws NullPointerException If the given class is <code>null</code>
     */
    static boolean maintainsPropertyChangeListeners(Class<?> c)
    {
        Objects.requireNonNull(c, "The class may not be null");
        Method addMethod = Methods.getMethodOptional(
            c, "addPropertyChangeListener", 
            PropertyChangeListener.class);
        if (addMethod == null || !isPublicInstanceMethod(addMethod))
        {
            return false;
        }
        Method removeMethod = Methods.getMethodOptional(
            c, "removePropertyChangeListener", 
            PropertyChangeListener.class);
        if (removeMethod == null || !isPublicInstanceMethod(removeMethod))
        {
            return false;
        }
        return true;
    }
    
    /**
     * Tries to add the given PropertyChangeListener to the given target
     * object. 
     * If the given target object does not
     * {@link #maintainsPropertyChangeListeners(Class)
     * maintain PropertyChangeListeners}, then nothing is done.
     * 
     * @param target The target object
     * @param propertyChangeListener The PropertyChangeListener to add
     * @throws IllegalArgumentException If the attempt to invoke the method
     * for adding the given listener failed.
     * @throws NullPointerException If any argument is <code>null</code>
     */
    public static void tryAddPropertyChangeListenerUnchecked(
        Object target, PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(target, "The target may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        if (maintainsPropertyChangeListeners(target.getClass()))
        {
            PropertyChangeUtils.addPropertyChangeListenerUnchecked(
                target, propertyChangeListener);
        }
    }

    /**
     * Add the given PropertyChangeListener to the given target object.
     * If the given target object does not
     * {@link #maintainsPropertyChangeListeners(Class)
     * maintain PropertyChangeListeners}, or the invocation of the method to 
     * add such a listener caused an error, a {@link IllegalArgumentException}
     * will be thrown.
     * 
     * @param target The target object
     * @param propertyChangeListener The PropertyChangeListener to add
     * @throws IllegalArgumentException If the given object does not maintain
     * PropertyChangeListeners, or the attempt to invoke the method
     * for adding the given listener failed.
     * @throws NullPointerException If any argument is <code>null</code>
     */
    private static void addPropertyChangeListenerUnchecked(
        Object target, PropertyChangeListener propertyChangeListener)
    {
        Class<?> c = target.getClass();
        if (!maintainsPropertyChangeListeners(c))
        {
            throw new IllegalArgumentException(
                "Class "+c+" does not maintain " +
                "PropertyChangeListeners");
        }
        Method addMethod = Methods.getMethodUnchecked(
            c, "addPropertyChangeListener", 
            PropertyChangeListener.class);
        Methods.invokeUnchecked(addMethod, target, 
            propertyChangeListener);
    }

    /**
     * Tries to remove the given PropertyChangeListener from the given
     * target object. 
     * If the given target object does not
     * {@link #maintainsPropertyChangeListeners(Class)
     * maintain PropertyChangeListeners}, then nothing is done.
     * 
     * @param target The target object
     * @param propertyChangeListener The PropertyChangeListener to remove
     * @throws IllegalArgumentException If the attempt to invoke the method
     * for removing the given listener failed.
     * @throws NullPointerException If any argument is <code>null</code>
     */
    public static void tryRemovePropertyChangeListenerUnchecked(
        Object target, PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(target, "The target may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        if (maintainsPropertyChangeListeners(target.getClass()))
        {
            PropertyChangeUtils.removePropertyChangeListenerUnchecked(
                target, propertyChangeListener);
        }
    }

    /**
     * Remove the given PropertyChangeListener to the given target object.
     * If the given target object does not
     * {@link #maintainsPropertyChangeListeners(Class)
     * maintain PropertyChangeListeners}, or the invocation of the method to 
     * remove such a listener caused an error, a 
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param target The target object
     * @param propertyChangeListener The PropertyChangeListener to remove
     * @throws IllegalArgumentException If the given object does not maintain
     * PropertyChangeListeners, or the attempt to invoke the method
     * for removing the given listener failed.
     */
    private static void removePropertyChangeListenerUnchecked(
        Object target, PropertyChangeListener propertyChangeListener)
    {
        Class<?> c = target.getClass();
        if (!maintainsPropertyChangeListeners(c))
        {
            throw new IllegalArgumentException(
                "Class "+c+" does not maintain " +
                "PropertyChangeListeners");
        }
        Method removeMethod = Methods.getMethodUnchecked(
            c, "removePropertyChangeListener", 
            PropertyChangeListener.class);
        Methods.invokeUnchecked(removeMethod, target, 
            propertyChangeListener);
    }

    
    /**
     * Returns whether the given class seems to maintain PropertyChangeListener
     * instances that are associated with named properties. That is, whether
     * the given class has the public instance methods
     * <code>addPropertyChangeListener(String, PropertyChangeListener)</code>
     * and
     * <code>removePropertyChangeListener(String, PropertyChangeListener)</code>
     * 
     * @param c The class
     * @return Whether the given class maintains named PropertyChangeListeners
     * @throws NullPointerException If the given class is <code>null</code>
     */
    static boolean maintainsNamedPropertyChangeListeners(Class<?> c)
    {
        Objects.requireNonNull(c, "The class may not be null");
        Method addMethod = Methods.getMethodOptional(
            c, "addPropertyChangeListener", 
            String.class, PropertyChangeListener.class);
        if (addMethod == null || !isPublicInstanceMethod(addMethod))
        {
            return false;
        }
        Method removeMethod = Methods.getMethodOptional(
            c, "removePropertyChangeListener", 
            String.class, PropertyChangeListener.class);
        if (removeMethod == null || !isPublicInstanceMethod(removeMethod))
        {
            return false;
        }
        return true;
    }

    /**
     * Tries to add the given PropertyChangeListener to the given target
     * object. 
     * If the given target object does not
     * {@link #maintainsNamedPropertyChangeListeners(Class)
     * maintain named PropertyChangeListeners}, then nothing is done.
     * 
     * @param target The target object
     * @param propertyName The property name
     * @param propertyChangeListener The PropertyChangeListener to add
     * @throws IllegalArgumentException If the attempt to invoke the method
     * for removing the given listener failed.
     * @throws NullPointerException If any argument is <code>null</code>
     */
    public static void tryAddNamedPropertyChangeListenerUnchecked(
        Object target, String propertyName, 
        PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(target, "The target may not be null");
        Objects.requireNonNull(propertyName, 
            "The propertyName may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        if (maintainsNamedPropertyChangeListeners(target.getClass()))
        {
            PropertyChangeUtils.addNamedPropertyChangeListenerUnchecked(
                target, propertyName, propertyChangeListener);
        }
    }

    /**
     * Add the given PropertyChangeListener to the given target object.
     * If the given target object does not 
     * {@link #maintainsNamedPropertyChangeListeners(Class)
     * maintain named PropertyChangeListeners}, or the invocation of 
     * the method to add such a listener caused an error, a 
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param target The target object
     * @param propertyName The property name
     * @param propertyChangeListener The PropertyChangeListener to add
     * @throws IllegalArgumentException If the given object does not maintain
     * named PropertyChangeListener, or the attempt to invoke the method
     * for adding the given listener failed.
     */
    private static void addNamedPropertyChangeListenerUnchecked(
        Object target, String propertyName, 
        PropertyChangeListener propertyChangeListener)
    {
        Class<?> c = target.getClass();
        if (!maintainsNamedPropertyChangeListeners(c))
        {
            throw new IllegalArgumentException(
                "Class "+c+" does not maintain " +
                "named PropertyChangeListeners");
        }
        Method addMethod = Methods.getMethodUnchecked(
            c, "addPropertyChangeListener", 
            String.class, PropertyChangeListener.class);
        Methods.invokeUnchecked(addMethod, target, 
            propertyName, propertyChangeListener);
    }

    /**
     * Tries to remove the given PropertyChangeListener from the given 
     * target object.
     * If the given target object does not
     * {@link #maintainsNamedPropertyChangeListeners(Class)
     * maintain named PropertyChangeListeners}, then nothing is done.
     * 
     * @param target The target object
     * @param propertyName The property name
     * @param propertyChangeListener The PropertyChangeListener to remove
     * @throws IllegalArgumentException If the attempt to invoke the method
     * for removing the given listener failed.
     * @throws NullPointerException If any argument is <code>null</code>
     */
    public static void tryRemoveNamedPropertyChangeListenerUnchecked(
        Object target, String propertyName, 
        PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(target, "The target may not be null");
        Objects.requireNonNull(propertyName, 
            "The propertyName may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        if (maintainsNamedPropertyChangeListeners(target.getClass()))
        {
            PropertyChangeUtils.removeNamedPropertyChangeListenerUnchecked(
                target, propertyName, propertyChangeListener);
        }
    }

    /**
     * Remove the given PropertyChangeListener to the given target object.
     * If the given target object does not
     * {@link #maintainsNamedPropertyChangeListeners(Class)
     * maintain named PropertyChangeListeners}, or the invocation of the 
     * method to remove such a listener caused an error, a 
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param target The target object
     * @param propertyName The property name
     * @param propertyChangeListener The PropertyChangeListener to remove
     * @throws IllegalArgumentException If the given object does not maintain
     * named PropertyChangeListener, or the attempt to invoke the method
     * for removing the given listener failed.
     */
    private static void removeNamedPropertyChangeListenerUnchecked(
        Object target, String propertyName, 
        PropertyChangeListener propertyChangeListener)
    {
        Class<?> c = target.getClass();
        if (!maintainsNamedPropertyChangeListeners(c))
        {
            throw new IllegalArgumentException(
                "Class "+c+" does not maintain " +
                "named PropertyChangeListeners");
        }
        Method removeMethod = Methods.getMethodUnchecked(
            c, "removePropertyChangeListener", 
            String.class, PropertyChangeListener.class);
        Methods.invokeUnchecked(removeMethod, target, 
            propertyName, propertyChangeListener);
    }

    
    /**
     * Returns whether the modifiers of the given method show that it is
     * a public instance method. 
     * 
     * @param method The method
     * @return Whether the given method is a public instance method
     */
    private static boolean isPublicInstanceMethod(Method method)
    {
        return Modifier.isPublic(method.getModifiers()) &&
            !Modifier.isStatic(method.getModifiers());
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private PropertyChangeUtils()
    {
        // Private constructor to prevent instantiation
    }
    
}
