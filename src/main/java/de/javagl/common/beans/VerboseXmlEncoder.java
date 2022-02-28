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
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An XML Encoder that writes all properties of given Java Beans explicitly,
 * even if they still have their default values.  
 */
class VerboseXmlEncoder extends XMLEncoder
{
    /**
     * The logger used in this class
     */
    static final Logger logger = 
        Logger.getLogger(VerboseXmlEncoder.class.getName());
    
    /**
     * Default constructor
     * 
     * @param out The target stream
     */
    VerboseXmlEncoder(OutputStream out)
    {
        super(out);
    }

    @Override
    public void writeStatement(Statement oldStm) 
    {
        Object[] arguments = oldStm.getArguments();
        for (int i=0; i<arguments.length; i++)
        {
            if (arguments[i] instanceof Enum)
            {
                forceWritingEnumValue(this, arguments[i]);
            }
        }
        super.writeStatement(oldStm);
    }
    
    @Override
    public PersistenceDelegate getPersistenceDelegate(Class<?> type)
    {
        PersistenceDelegate p = super.getPersistenceDelegate(type);
        if (p.getClass().equals(DefaultPersistenceDelegate.class))
        {
            p = new FullPersistenceDelegate();
            setPersistenceDelegate(type, p);
        }
        return p;
    }
    
    /**
     * Use a nasty reflection hack to make sure that the given object is
     * always written explicitly, without using "idref", in the given 
     * VerboseXmlEncoder
     * 
     * @param encoder The encoder
     * @param object The object
     */
    static void forceWritingEnumValue(
        VerboseXmlEncoder encoder, Object object)
    {
        Field valueToExpressionField = null;
        Field refsField = null;
        try
        {
            Class<?> encoderClass = encoder.getClass().getSuperclass();
            valueToExpressionField = 
                encoderClass.getDeclaredField("valueToExpression");
            valueToExpressionField.setAccessible(true);
            Object valueToExpressionObject = 
                valueToExpressionField.get(encoder);
            Map<?,?> valueToExpression = (Map<?,?>)valueToExpressionObject;
            Object valueData = valueToExpression.get(object);
            if (valueData != null)
            {
                Class<?> valueDataClass = valueData.getClass(); 
                refsField = valueDataClass.getDeclaredField("refs");
                refsField.setAccessible(true);
                refsField.setInt(valueData, 0);
            }
        }
        catch (NoSuchFieldException e)
        {
            logger.warning(e.toString());
        }
        catch (SecurityException e)
        {
            logger.warning(e.toString());
        }
        catch (IllegalArgumentException e)
        {
            logger.warning(e.toString());
        }
        catch (IllegalAccessException e)
        {
            logger.warning(e.toString());
        }
        finally
        {
            if (valueToExpressionField != null)
            {
                valueToExpressionField.setAccessible(false);
            }
            if (refsField != null)
            {
                refsField.setAccessible(false);
            }
        }
    }
    
}