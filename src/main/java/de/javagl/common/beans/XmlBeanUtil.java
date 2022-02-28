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

import de.javagl.common.xml.XmlException;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * Utility methods related to XML handling of Java Beans
 */
public class XmlBeanUtil
{
    /**
     * Write the XML describing the given bean to the given output stream, 
     * as it is done by an <code>XMLEncoder</code>, but including all 
     * properties, even if they still have their default values. The 
     * caller is responsible for closing the given stream.
     * 
     * @param object The bean object
     * @param outputStream The stream to write to
     * @throws XmlException If there is an error while encoding the object
     */
    public static void writeFullBeanXml(
        Object object, OutputStream outputStream)
    {
        XMLEncoder encoder = XmlEncoders.createVerbose(outputStream);
        encoder.setExceptionListener(new ExceptionListener()
        {
            @Override
            public void exceptionThrown(Exception e)
            {
                throw new XmlException("Could not encode object", e);
            }
        });
        encoder.writeObject(object);
        encoder.flush();
        encoder.close();
    }
    
    /**
     * Create the XML string describing the given bean, as created by
     * an <code>XMLEncoder</code>, but including all properties, even 
     * if they still have their default values.
     * 
     * @param object The bean object
     * @return The string
     * @throws XmlException If there is an error while encoding the object
     */
    public static String createFullBeanXmlString(Object object)
    {
        ByteArrayOutputStream stream = null;
        try
        {
            stream = new ByteArrayOutputStream();
            writeFullBeanXml(object, stream);
            return stream.toString();
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e)
                {
                    throw new XmlException("Could not close the stream", e);
                }
            }
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private XmlBeanUtil()
    {
        // Private constructor to prevent instantiation
    }
}
