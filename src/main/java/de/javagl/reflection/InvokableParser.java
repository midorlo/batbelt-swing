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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing information about a Method or a Constructor 
 * (i.e. an Invokable entity) from a string. 
 */
class InvokableParser
{
    /**
     * The result that has been parsed
     */
    static final class InvokableInfo
    {
        /**
         * The fully qualified name of the invokable
         */
        private final String fullyQualifiedName;
        
        /**
         * The parameter types of the invokable
         */
        private final Class<?> parameterTypes[];
        
        /**
         * Creates a new info with the given parameters
         *  
         * @param fullyQualifiedName The fully qualified name of the invokable
         * @param parameterTypes The parameter types of the invokable
         */
        InvokableInfo(String fullyQualifiedName, Class<?> parameterTypes[])
        {
            this.fullyQualifiedName = fullyQualifiedName;
            this.parameterTypes = parameterTypes.clone();
        }
        
        /**
         * Returns the parameter types of the invokable
         * 
         * @return The parameter types of the invokable
         */
        String getFullyQualifiedName()
        {
            return fullyQualifiedName;
        }
        
        /**
         * Returns the parameter types of the invokable
         * 
         * @return The parameter types of the invokable
         */
        Class<?>[] getParameterTypes()
        {
            return parameterTypes.clone();
        }
    }
    
    /**
     * Parse the {@link InvokableInfo} from the given string. The given 
     * string must be the string that is obtained from a method by calling 
     * {@link Method#toString()} or {@link Method#toGenericString()}, or
     * from a Constructor by calling {@link Constructor#toString()} or
     * {@link Constructor#toGenericString()}.
     * 
     * @param fullString The full string
     * @return The {@link InvokableInfo}
     * @throws ReflectionException If the invokable can not be parsed
     * for any reason. Either because any parameter type can not be 
     * parsed, or because the input string is otherwise invalid.
     */
    static InvokableInfo parse(String fullString)
    {
        int openingIndex = fullString.indexOf('(');
        int closingIndex = fullString.indexOf(')');
        if (openingIndex == -1 || closingIndex == -1)
        {
            throw new ReflectionException(
                "No method or constructor in input string: " + fullString);
        }
        
        // Obtain the part before the first opening "(" bracket 
        // as a list of strings
        String header = fullString.substring(0, openingIndex);
        List<String> headerStrings = splitHeader(header);
        
        // Obtain the list of type parameter names for this method
        List<String> typeParameterNames = 
            findTypeParameterNames(headerStrings);
        
        // Fetch the last element of this list, which is of the form
        // com.domain.ClassName.methodName (for Methods) or
        // com.domain.ClassName (for Constructors)
        String fullyQualifiedName = headerStrings.get(headerStrings.size() - 1);
        
        // Obtain the parameter types as a list of strings
        String parameterTypesString = 
            fullString.substring(openingIndex + 1, closingIndex);
        String parameterTypeStringArray[] = parameterTypesString.split(",");
        List<String> parameterTypeStrings = new ArrayList<String>();
        for (String s : parameterTypeStringArray)
        {
            String st = s.trim();
            if (st.length() != 0)
            {
                parameterTypeStrings.add(st);
            }
        }

        // Try to find the parameter types
        List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
        for (String parameterTypeString : parameterTypeStrings)
        {
            if (typeParameterNames.contains(parameterTypeString))
            {
                parameterTypes.add(Object.class);
            }
            else
            {
                String typeString = removeTypeParameters(parameterTypeString);
                Class<?> parameterType = Types.parseTypeUnchecked(typeString);
                parameterTypes.add(parameterType);
            }
        }
        Class<?> parameterTypesArray[] = 
            parameterTypes.toArray(new Class<?>[0]);
        
        InvokableInfo result = 
            new InvokableInfo(fullyQualifiedName, parameterTypesArray);
        return result;
    }

    /**
     * Remove type parameters from a String representing a type. 
     * For example, <code>java.util.Collection&lt;? extends E&gt;</code> 
     * will be converted to <code>java.util.Collection</code> 
     * 
     * @param typeString The type string
     * @return The type string without type parameters
     */
    private static String removeTypeParameters(String typeString)
    {
        int openingIndex = typeString.indexOf('<');
        if (openingIndex == -1)
        {
            return typeString;
        }
        return typeString.substring(0, openingIndex);
    }
    
    /**
     * Split the String that contains the header of a method or constructor
     * into a list of tokens. The header here is the part before the first
     * opening bracket "(" of the string that is obtained from a method by 
     * calling {@link Method#toString()} or {@link Method#toGenericString()},
     * or {@link Constructor#toString()} or 
     * {@link Constructor#toGenericString()}.
     * <br>
     * The first entries of the returned list will be the modifiers like
     * <code>public</code> or <code>static</code>. These may optionally
     * be followed by ONE entry with comma-separated type parameters,
     * enclosed in angular brackets, like <code>&lt;T, S&gt;</code>.
     * The last element of this list will be the fully qualified method-
     * name in the form <code>com.domain.ClassName.methodName</code>,
     * or the fully qualified constructor name in the form
     * <code>com.domain.ClassName</code>.
     * 
     * @param header The header 
     * @return The list of tokens for the header
     */
    private static List<String> splitHeader(String header)
    {
        List<String> methodHeaders = new ArrayList<String>();
        boolean inBrackets = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < header.length(); i++)
        {
            char c = header.charAt(i);
            if (c == '<')
            {
                inBrackets = true;
                sb.append("<");
            }
            else if (c == '>')
            {
                inBrackets = false;
                sb.append(">");
            }
            else if (!inBrackets && c == ' ')
            {
                methodHeaders.add(sb.toString().trim());
                sb = new StringBuilder();
            }
            else
            {
                sb.append(c);                
            }
        }
        if (sb.length() > 0)
        {
            methodHeaders.add(sb.toString().trim());
        }
        return methodHeaders;
    }
    
    /**
     * Extracts type parameter names from the given list of header elements.
     * The given list may have been created with {@link #splitHeader(String)}.
     * If the given list contains a string that starts with an opening 
     * bracket <code>"&lt;"</code> and ends with a closing bracket
     * <code>"&gt;"</code>, then the part of the string that is enclosed 
     * by these brackets will be interpreted as a comma-separated list of
     * type parameter names, and a list containing these type parameter 
     * names will be returned.
     * For example, if the given list contains the String
     * <code>"&lt;A, B&gt;"</code>, then a list containing <code>"A"</code>
     * and <code>"B"</code> will be returned.
     * 
     * @param headerStrings The header strings
     * @return The list of type parameter names
     */
    private static List<String> findTypeParameterNames(
        List<String> headerStrings)
    {
        List<String> typeParameterNames = new ArrayList<String>();
        for (String s : headerStrings)
        {
            if (s.startsWith("<") && s.endsWith(">"))
            {
                String t = s.substring(1, s.length() - 1);
                String names[] = t.split(",");
                for (String name : names)
                {
                    typeParameterNames.add(name.trim());
                }
            }
        }
        return typeParameterNames;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private InvokableParser()
    {
        // Private constructor to prevent instantiation
    }
    
    
}
