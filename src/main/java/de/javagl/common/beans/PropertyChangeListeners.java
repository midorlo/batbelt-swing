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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods related to property change listeners
 */
public class PropertyChangeListeners
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(PropertyChangeListeners.class.getName());

    /**
     * Attaches a deep property change listener to the given object, that
     * generates logging information about the property change events,
     * and prints them as INFO log messages.
     * 
     * @param object The object
     */
    public static void addDeepLogger(Object object)
    {
        addDeepLogger(object, Level.INFO);
    }

    /**
     * Attaches a deep property change listener to the given object, that
     * generates logging information about the property change events,
     * and prints them as log messages.
     * 
     * @param object The object
     * @param level The log level
     */
    public static void addDeepLogger(Object object, Level level)
    {
        addDeepLogger(object, m -> logger.log(level, m));
    }
    
    /**
     * Attaches a deep property change listener to the given object, that
     * generates logging information about the property change events,
     * and prints them to the standard output.
     * 
     * @param object The object
     */
    public static void addDeepConsoleLogger(Object object)
    {
        addDeepLogger(object, m -> System.out.println(m));
    }
    
    /**
     * Attaches a deep property change listener to the given object, that
     * generates logging information about the property change events,
     * and passes them to the given consumer.
     * 
     * @param object The object
     * @param consumer The log message consumer. May not be <code>null</code>.
     */
    public static void addDeepLogger(
        Object object, Consumer<? super String> consumer)
    {
        Objects.requireNonNull(consumer, "The consumer may not be null");
        PropertyChangeListener propertyChangeListener = 
            new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                Object source = event.getSource();
                String propertyName = event.getPropertyName();
                Object oldValue = event.getOldValue();
                String oldValueString = createLoggingString(oldValue);
                Object newValue = event.getNewValue();
                String newValueString = createLoggingString(newValue);
                String message = 
                    "Property " + propertyName + " changed from " + 
                    oldValueString + " to " + newValueString + " in " + source;
                consumer.accept(message);
            }
        };
        addDeepPropertyChangeListener(object, propertyChangeListener);
    }
    
    /**
     * Create a string suitable for logging the given object
     * 
     * @param object The object
     * @return The string
     */
    private static String createLoggingString(Object object)
    {
        if (object == null)
        {
            return "null";
        }
        Class<? extends Object> objectClass = object.getClass();
        if (!objectClass.isArray())
        {
            return String.valueOf(object);
        }
        StringBuilder sb = new StringBuilder();
        int length = Array.getLength(object);
        sb.append("[");
        for (int i = 0; i < length; i++)
        {
            if (i > 0)
            {
                sb.append(", ");
            }
            Object element = Array.get(object, i);
            sb.append(createLoggingString(element));
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * A class representing an object and all property change listeners
     * that have been attached to it using 
     * {@link PropertyChangeListeners#addDeepPropertyChangeListener}.<br>
     */
    public static class ObservedObject
    {
        /**
         * The object
         */
        private final Object object;
        
        /**
         * The actual property change listener
         */
        private final PropertyChangeListener propertyChangeListener;
        
        /**
         * The property change listener that forwards the actual property
         * change listener to child objects
         */
        private final PropertyChangeListener forwardingPropertyChangeListener;
        
        /**
         * Creates a new observed object
         * 
         * @param object The object
         * @param propertyChangeListener The property change listener
         * @param forwardingPropertyChangeListener The forwarding property
         * change listener
         */
        ObservedObject(Object object, 
            PropertyChangeListener propertyChangeListener,
            PropertyChangeListener forwardingPropertyChangeListener)
        {
            this.object = object;
            this.propertyChangeListener = propertyChangeListener;
            this.forwardingPropertyChangeListener = 
                forwardingPropertyChangeListener;
        }
        
        /**
         * This method will remove all property change listeners from the 
         * object that have been attached during the call to 
         * {@link PropertyChangeListeners#addDeepPropertyChangeListener}
         * that created this observed object.
         */
        public void detach()
        {
            removeDeepPropertyChangeListener(
                object, propertyChangeListener);
            removeDeepPropertyChangeListener(
                object, forwardingPropertyChangeListener);
        }
        
    }
    
    /**
     * Add the given property change listener to the given object and all 
     * its sub-objects, and make sure that the property change listener
     * will be attached to all sub-objects that are set, and removed from
     * all sub-objects that are removed.<br>
     * <br>  
     * The returned {@link ObservedObject} instance may be used to 
     * detach all property change listeners that have been attached
     * with this call:
     * <pre><code>
     * ObservedObject observedObject =
     *     PropertyChangeListeners.addDeepPropertyChangeListener(someObject, p);
     *     
     * // Set some properties, informing the PropertyChangeListeners
     * someObject.setFoo("foo");
     * someObject.getInnerObject().setBar("bar");
     * 
     * // Detach the deep property change listener:
     * observedObject.detach(); 
     * </code></pre>
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     * @return The {@link ObservedObject}
     */
    public static ObservedObject addDeepPropertyChangeListener(
        Object object, PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(object, "The object may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        
        PropertyChangeListener forwardingPropertyChangeListener = 
            new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                Object oldValue = event.getOldValue();
                Object newValue = event.getNewValue();
                removeRecursive(oldValue, propertyChangeListener);
                removeRecursive(oldValue, this);
                addRecursive(newValue, propertyChangeListener);
                addRecursive(newValue, this);
            }
        };
        
        addRecursive(object, propertyChangeListener);
        addRecursive(object, forwardingPropertyChangeListener);
        
        ObservedObject observedObject = new ObservedObject(object, 
            propertyChangeListener, forwardingPropertyChangeListener);
        return observedObject;
    }
    
    /**
     * Remove the given property change listener from the given object
     * and all its sub-objects.
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     */
    public static void removeDeepPropertyChangeListener(
        Object object, PropertyChangeListener propertyChangeListener)
    {
        Objects.requireNonNull(object, "The object may not be null");
        Objects.requireNonNull(propertyChangeListener, 
            "The propertyChangeListener may not be null");
        removeRecursive(object, propertyChangeListener);
    }
    
    /**
     * Recursively add the given property change listener to the given
     * object and all its sub-objects
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     */
    private static void addRecursive(
        Object object, PropertyChangeListener propertyChangeListener)
    {
        addRecursive(object, propertyChangeListener, 
            new LinkedHashSet<Object>());
    }
    
    /**
     * Recursively add the given property change listener to the given
     * object and all its sub-objects
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     * @param processedObjects The set of objects that have already been
     * processed.
     */
    private static void addRecursive(
        Object object, PropertyChangeListener propertyChangeListener,
        Set<Object> processedObjects)
    {
        Objects.requireNonNull(processedObjects, 
            "The processedObjects may not be null");
        if (object == null)
        {
            return;
        }
        if (processedObjects.contains(object))
        {
            return;
        }
        processedObjects.add(object);
        if (object.getClass().isArray())
        {
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++)
            {
                Object element = Array.get(object, i);
                addRecursive(element, 
                    propertyChangeListener, processedObjects);
            }
            return;
        }
        PropertyChangeUtils.tryAddPropertyChangeListenerUnchecked(
            object, propertyChangeListener);
        List<String> propertyNames =
            BeanUtils.getMutablePropertyNamesOptional(object.getClass());
        for (String propertyName : propertyNames)
        {
            Object value = 
                BeanUtils.invokeReadMethodOptional(object, propertyName);
            addRecursive(value, propertyChangeListener, processedObjects);
        }
    }

    /**
     * Recursively remove the given property change listener from the given
     * object and all its sub-objects
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     */
    private static void removeRecursive(
        Object object, PropertyChangeListener propertyChangeListener)
    {
        removeRecursive(object, propertyChangeListener, 
            new LinkedHashSet<Object>());
    }
    
    /**
     * Recursively remove the given property change listener from the given
     * object and all its sub-objects
     * 
     * @param object The object
     * @param propertyChangeListener The property change listener
     * @param processedObjects The set of objects that have already been
     * processed.
     */
    private static void removeRecursive(
        Object object, PropertyChangeListener propertyChangeListener,
        Set<Object> processedObjects)
    {
        Objects.requireNonNull(processedObjects, 
            "The processedObjects may not be null");
        if (object == null)
        {
            return;
        }
        if (processedObjects.contains(object))
        {
            return;
        }
        processedObjects.add(object);
        if (object.getClass().isArray())
        {
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++)
            {
                Object element = Array.get(object, i);
                removeRecursive(element, 
                    propertyChangeListener, processedObjects);
            }
            return;
        }
        PropertyChangeUtils.tryRemovePropertyChangeListenerUnchecked(
            object, propertyChangeListener);
        List<String> propertyNames =
            BeanUtils.getMutablePropertyNamesOptional(object.getClass());
        for (String propertyName : propertyNames)
        {
            Object value = 
                BeanUtils.invokeReadMethodOptional(object, propertyName);
            removeRecursive(value, propertyChangeListener, processedObjects);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private PropertyChangeListeners()
    {
        // Private constructor to prevent instantiation
    }
    
}
