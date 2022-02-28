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

/**
 * An unchecked exception which is used to wrap all checked 
 * exceptions that may happen during a reflection operation.
 */
public final class ReflectionException extends RuntimeException
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -2665638988521501287L;
    
    /**
     * Creates a ReflectionException with the given message
     * 
     * @param message The message
     */
    public ReflectionException(String message)
    {
        super(message);
    }

    /**
     * Creates a ReflectionException with the given message and cause
     * 
     * @param message The message
     * @param cause The cause
     */
    public ReflectionException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * Creates a ReflectionException with the given cause
     * 
     * @param cause The cause
     */
    public ReflectionException(Throwable cause)
    {
        super(cause);
    }
    
}
