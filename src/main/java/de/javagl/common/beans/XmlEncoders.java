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

import java.beans.XMLEncoder;
import java.io.OutputStream;

/**
 * Methods to create special XML Encoder instances
 */
public class XmlEncoders
{
    /**
     * Creates an XMLEncoder that writes all properties of bean objects
     * explicitly, even when they still have their default values.
     * 
     * @param outputStream The target stream
     * @return The verbose XMLEncoder
     */
    public static XMLEncoder createVerbose(OutputStream outputStream)
    {
        return new VerboseXmlEncoder(outputStream);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private XmlEncoders()
    {
        // Private constructor to prevent instantiation
    }
}
