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

import java.beans.*;
import java.lang.reflect.Method;

/**
 * Implementation of a PersistenceDelegate that serializes all properties
 * of a Java Bean, even if they still have their default values.<br>
 * <br>
 * It may be registered as the PersistenceDelegate of an XMLEncoder for
 * a particular bean class:
 * <code><pre>
 * xmlEncoder.setPersistenceDelegate(ExampleBean.class,
 *     new FullPersistenceDelegate());
 * </pre></code>
 */
class FullPersistenceDelegate extends PersistenceDelegate
{
    /**
     * Default constructor
     */
    FullPersistenceDelegate()
    {
        // Default constructor
    }
    
    @Override
    protected Expression instantiate(Object oldInstance, Encoder encoder)
    {
        return new Expression(
            oldInstance, oldInstance.getClass(), "new", null);
    }

    @Override
    protected void initialize(Class<?> type, Object oldInstance,
        Object newInstance, Encoder encoder)
    {
        super.initialize(type, oldInstance, newInstance, encoder);
        if (oldInstance.getClass() == type)
        {
            initializeProperties(type, oldInstance, encoder);
        }
    }
    
    /**
     * Write all statements to initialize the properties of the given 
     * Java Bean Type, based on the given instance, using the given
     * encoder
     *   
     * @param type The Java Bean Type
     * @param oldInstance The base instance
     * @param encoder The encoder
     */
    private void initializeProperties(
        Class<?> type, Object oldInstance, Encoder encoder)
    {
        BeanInfo info = null;
        try
        {
            info = Introspector.getBeanInfo(type);
        }
        catch (IntrospectionException ie)
        {
            encoder.getExceptionListener().exceptionThrown(ie);
            return;
        }
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        for (int i = 0; i < pds.length; ++i)
        {
            try
            {
                initializeProperty(type, pds[i], oldInstance, encoder);
            }
            catch (Exception e)
            {
                encoder.getExceptionListener().exceptionThrown(e);
            }
        }
    }
    
    /**
     * Write the statement to initialize the specified property of the given 
     * Java Bean Type, based on the given instance, using the given
     * encoder
     *   
     * @param type The Java Bean Type
     * @param pd The property descriptor
     * @param oldInstance The base instance
     * @param encoder The encoder
     * @throws Exception If the value can not be obtained
     */
    private void initializeProperty(
        Class<?> type, PropertyDescriptor pd, Object oldInstance, Encoder encoder) 
        throws Exception
    {
        Method getter = pd.getReadMethod();
        Method setter = pd.getWriteMethod();
        if (getter != null && setter != null)
        {
            Expression oldGetExpression =
                new Expression(oldInstance, getter.getName(), new Object[] {});
            Object oldValue = oldGetExpression.getValue();
            Statement setStatement =
                new Statement(oldInstance, setter.getName(), 
                    new Object[] { oldValue });
            encoder.writeStatement(setStatement);
        }
    }
    
}