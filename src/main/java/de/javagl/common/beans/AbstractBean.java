/*
 * www.javagl.de - Common
 *
 * Copyright (c) 2012-2015 Marco Hutter - http://www.javagl.de
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
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Abstract base implementation of a bean, maintaining a 
 * PropertyChangeSupport.<br>
 * <br>
 * In order to easily create a bean in Eclipse:
 * <ul>
 *   <li>Inherit from this class</li>
 *   <li>Automatically create setters and getters for all fields</li>
 *   <li>Open the Find/Replace dialog<br>
 *     <ul>
 *       <li>Enable "Regular Expressions"</li>
 *       <li>Find: <code>(this.+) = (\w+);</code></li>
 *       <li>
 *         Replace with: <code>firePropertyChange("$2", $1, $1 = $2);</code>
 *       </li>
 *     </ul>
 *   </li>
 * </ul>
 */
public abstract class AbstractBean implements Serializable
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -5434761913464888763L;
    
    /**
     * The property change support
     */
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Default constructor
     */
    protected AbstractBean()
    {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Informs all registered PropertyChangeListeners about the
     * change in the specified property. No event is fired if 
     * old and new are equal and non-null. 
     * 
     * @param propertyName The name of the property
     * @param oldValue The old value of the property
     * @param newValue The new value of the property
     */
    protected final void firePropertyChange(
        String propertyName, Object oldValue, Object newValue)
    {
        propertyChangeSupport.firePropertyChange(
            propertyName, oldValue, newValue);
    }

    /**
     * Add the given listener to be informed about property changes
     * 
     * @param propertyChangeListener The listener to add
     */
    public final void addPropertyChangeListener(
        PropertyChangeListener propertyChangeListener)
    {
        propertyChangeSupport.addPropertyChangeListener(
            propertyChangeListener);
    }

    /**
     * Remove the given listener
     * 
     * @param propertyChangeListener The listener to remove
     */
    public final void removePropertyChangeListener(
        PropertyChangeListener propertyChangeListener)
    {
        propertyChangeSupport.removePropertyChangeListener(
            propertyChangeListener);
    }

    /**
     * Add the given listener to be informed about property changes
     * 
     * @param propertyName The property name
     * @param propertyChangeListener The listener to add
     */
    public final void addPropertyChangeListener(String propertyName,
        PropertyChangeListener propertyChangeListener)
    {
        propertyChangeSupport.addPropertyChangeListener(
            propertyName, propertyChangeListener);        
    }

    /**
     * Remove the given listener
     * 
     * @param propertyName The property name
     * @param propertyChangeListener The listener to remove
     */
    public final void removePropertyChangeListener(String propertyName,
        PropertyChangeListener propertyChangeListener)
    {
        propertyChangeSupport.removePropertyChangeListener(
            propertyName, propertyChangeListener);        
    }
}