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
package de.javagl.common.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Utility methods related to XML handling
 */
public class XmlUtils
{
    /**
     * A default XML document
     */
    private static Document defaultDocument = null;
    
    /**
     * Returns a default XML document
     * 
     * @return The default XML document
     */
    public static synchronized Document getDefaultDocument()
    {
        if (defaultDocument == null)
        {
            DocumentBuilderFactory documentBuilderFactory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = null;
            try
            {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
            }
            catch (ParserConfigurationException e)
            {
                // Can not happen, since it's the default configuration
                throw new XmlException("Could not create default document", e);
            }
            defaultDocument = documentBuilder.newDocument();
        }
        return defaultDocument;
    }
    
    /**
     * Creates a formatted String representation of the given XML node,
     * including the XML declaration
     * 
     * @param node The node
     * @return The formatted String representation of the given node
     * @throws XmlException If there was an error while writing. 
     * This should never be the case for this method, because the
     * XML is written into a String, and no exceptional situation
     * (like IOException) can happen here.
     */
    public static String toString(Node node)
    {
        StringWriter stringWriter = new StringWriter();
        write(node, stringWriter, 4, false);
        return stringWriter.toString();
    }

    /**
     * Creates a String representation of the given XML node
     * 
     * @param node The node
     * @param indentation The indentation. If this is not positive, then
     * no indentation will be performed
     * @param omitXmlDeclaration Whether the XML declaration should be omitted 
     * @return The formatted String representation of the given node
     * @throws XmlException If there was an error while writing. 
     * This should never be the case for this method, because the
     * XML is written into a String, and no exceptional situation
     * (like IOException) can happen here.
     */
    public static String toString(
        Node node, int indentation, boolean omitXmlDeclaration)
    {
        StringWriter stringWriter = new StringWriter();
        write(node, stringWriter, indentation, omitXmlDeclaration);
        return stringWriter.toString();
    }
    
    /**
     * Writes a formatted String representation of the given XML node,
     * including the XML declaration, to the given output stream. 
     * The caller is responsible for closing the given stream.
     * 
     * @param node The node
     * @param outputStream The output stream to write to
     * @throws XmlException If there was an error while writing
     */
    public static void write(Node node, OutputStream outputStream)
    {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        write(node, writer, 4, false);
    }
    
    /**
     * Writes a formatted String representation of the given XML node,
     * including the XML declaration, to the given output stream. 
     * The caller is responsible for closing the given stream.
     * 
     * @param node The node
     * @param outputStream The output stream to write to
     * @param indentation The indentation. If this is not positive, then
     * no indentation will be performed
     * @param omitXmlDeclaration Whether the XML declaration should be omitted 
     * @throws XmlException If there was an error while writing
     */
    public static void write(Node node, OutputStream outputStream,
        int indentation, boolean omitXmlDeclaration)
    {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        write(node, writer, indentation, omitXmlDeclaration);
    }
    
    
    /**
     * Writes a formatted String representation of the given XML node
     * to the given writer.
     * 
     * @param node The node 
     * @param writer The writer to write to
     * @param indentation The indentation. If this is not positive, then
     * no indentation will be performed
     * @param omitXmlDeclaration Whether the XML declaration should be omitted 
     * @throws XmlException If there was an error while writing
     */
    private static void write(
        Node node, Writer writer, int indentation, boolean omitXmlDeclaration)
    {
        TransformerFactory transformerFactory = 
            TransformerFactory.newInstance();
        if (indentation > 0)
        {
            transformerFactory.setAttribute("indent-number", indentation);
        }
        Transformer transformer = null;
        try
        {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException canNotHappen)
        {
            // Can not happen here
            throw new XmlException(
                "Could not create transformer", canNotHappen);
        } 
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(
            OutputKeys.INDENT, 
            propertyStringFor(indentation > 0));
        transformer.setOutputProperty(
            OutputKeys.OMIT_XML_DECLARATION, 
            propertyStringFor(omitXmlDeclaration));
        DOMSource source = new DOMSource(node);

        StreamResult xmlOutput = new StreamResult(writer);
        try
        {
            transformer.transform(source, xmlOutput);
        }
        catch (TransformerException e)
        {
            throw new XmlException("Could not transform node", e);
        }
    }
    
    /**
     * Returns "yes" for <code>true</code> and "no" for <code>false</code>
     * 
     * @param b The boolean
     * @return The string.
     */
    private static String propertyStringFor(boolean b)
    {
        return b ? "yes" : "no";
    }
    
    /**
     * Creates an XML node by reading the contents of the given input stream.
     * 
     * @param inputStream The input stream to read from
     * @return The parsed node
     * @throws XmlException If there was an error while reading
     */
    public static Node read(InputStream inputStream) throws XmlException
    {
        try
        {
            DocumentBuilderFactory documentBuilderFactory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = 
                documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Node node = document.getDocumentElement();
            return node;
        } 
        catch (ParserConfigurationException canNotHappen)
        {
            // Can not happen with a default configuration
            throw new XmlException("Could not create parser", canNotHappen);
        } 
        catch (SAXException e)
        {
            throw new XmlException("XML parsing error", e);
        }
        catch (IOException e)
        {
            throw new XmlException("IO error while reading XML", e);
        }

    }
    
    /**
     * Creates an XML node from the 
     * {@link #getDefaultDocument() default document} whose only child
     * is a text node that contains the string representation of the
     * given object
     * 
     * @param tagName The tag name for the node
     * @param contents The object whose string representation will be
     * the contents of the text node
     * @return The node
     */
    public static Node createTextNode(String tagName, Object contents)
    {
        Document d = XmlUtils.getDefaultDocument();
        Element node = d.createElement(tagName);
        node.appendChild(d.createTextNode(String.valueOf(contents)));
        return node;
    }
    
    
    /**
     * Returns the attribute with the given name from the given node.
     * If the respective attribute could not be obtained, the given
     * default value will be returned
     * 
     * @param node The node to obtain the attribute from
     * @param attributeName The name of the attribute
     * @param defaultValue The default value to return when the specified
     * attribute could not be obtained
     * @return The value of the attribute, or the default value
     */
    public static String getAttributeValue(
        Node node, String attributeName, String defaultValue)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode == null)
        {
            return defaultValue;
        }
        String value = attributeNode.getNodeValue();
        if (value == null)
        {
            return defaultValue;
        }
        return value;
    }
    
    
    /**
     * Returns the attribute with the given name from the given node.
     * 
     * @param node The node to obtain the attribute from
     * @param attributeName The name of the attribute
     * @return The value of the attribute
     * @throws XmlException If no value of the attribute with the given 
     * name could be obtained.
     */
    public static String getRequiredAttributeValue(
        Node node, String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode == null)
        {
            throw new XmlException(
                "No attribute with name \""+attributeName+"\" found");
        }
        String value = attributeNode.getNodeValue();
        if (value == null)
        {
            throw new XmlException(
                "Attribute with name \""+attributeName+"\" has no value");
        }
        return value;
    }
    
    
    
    /**
     * Removes any whitespace from the text nodes that are descendants
     * of the given node. Any text node that becomes empty due to this
     * (that is, any text node that only contained whitespaces) will
     * be removed.
     * 
     * @param node The node to remove whitespaces from
     */
    static void removeWhitespace(Node node)
    {
        NodeList childList = node.getChildNodes();
        List<Node> toRemove = new ArrayList<Node>();
        for (int i = 0; i < childList.getLength(); i++)
        {
            Node child = childList.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
            {
                String text = child.getTextContent();
                String trimmed = text.trim();
                if (trimmed.isEmpty())
                {
                    toRemove.add(child);
                }
                else if (trimmed.length() < text.length())
                {
                    child.setTextContent(trimmed);
                }
            }
            removeWhitespace(child);
        }
        for (Node c : toRemove)
        {
            node.removeChild(c);
        }
    }
    
    
    /**
     * Returns the first child of the given node with the given name (ignoring
     * upper/lower case), or <code>null</code> if no such child is found.
     *  
     * @param node The node
     * @param childNodeName The child node name
     * @return The child with the given name, or <code>null</code>
     */
    public static Node getFirstChild(Node node, String childNodeName)
    {
        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            String childName = child.getNodeName();
            if (childName.equalsIgnoreCase(childNodeName))
            {
                return child;
            }
        }
        return null;
    }
    
    /**
     * Returns the children of the given node with the given name (ignoring
     * upper/lower case).
     *  
     * @param node The node
     * @param childNodeName The child node name
     * @return The children with the given name
     */
    public static List<Node> getChildren(Node node, String childNodeName)
    {
        List<Node> children = new ArrayList<Node>();
        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            String childName = child.getNodeName();
            if (childName.equalsIgnoreCase(childNodeName))
            {
                children.add(child);
            }
        }
        return children;
    }
    

    /**
     * Verify that the given node is not <code>null</code>, and that its
     * name matches the expected tag name (ignoring upper/lowercase), 
     * and throw an XmlException if this is not the case.
     * 
     * @param node The node
     * @param expected The expected tag name
     * @throws XmlException If the node is <code>null</code>, or the 
     * node name does not match the expected name
     */ 
    public static void verifyNode(Node node, String expected)
    {
        if (node == null)
        {
            throw new XmlException(
                "Did not find <"+expected+"> node");
        }
        if (!node.getNodeName().equalsIgnoreCase(expected))
        {
            throw new XmlException(
                "Expected <"+expected+"> tag, " +
                "but found <"+node.getNodeName()+">");
        }
    }
    
    
    /**
     * Resolve the value in the given map whose key is the value of 
     * the specified attribute. 
     * 
     * @param <T> The type of the elements in the map
     * @param node The node
     * @param attributeName The name of the attribute
     * @param map The map 
     * @return The value in the map whose key is the value of the
     * specified attribute
     * @throws XmlException If either the given node does not contain
     * an attribute with the given name, or the map does not contain
     * a value for the respective attribute value
     */
    static <T> T resolveAttributeFromMap(
        Node node, String attributeName, Map<String, ? extends T> map)
    {
        String id = XmlUtils.getAttributeValue(node, attributeName, null);
        if (id == null)
        {
            throw new XmlException(
                "No attribute \""+attributeName+"\" found");
        }
        T result = map.get(id);
        if (result == null)
        {
            throw new XmlException(
                "Could not resolve value with id \""+id+"\"");
        }
        return result;
    }
    
    
    /**
     * Parse an int value from the first child of the given node.
     * 
     * @param node The node
     * @return The int value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * int value could be parsed
     */
    static int readInt(Node node)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read int value from null node");
        }
        String value = node.getFirstChild().getNodeValue();
        if (value == null)
        {
            throw new XmlException(
                "Tried to read int value from null value");
        }
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            throw new XmlException(
                "Expected int value, found \""+value+"\"", e);
        }
    }

    /**
     * Parse a double value from the first child of the given node.
     * 
     * @param node The node
     * @return The double value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * double value could be parsed
     */
    static double readDouble(Node node)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read double value from null node");
        }
        String value = node.getFirstChild().getNodeValue();
        if (value == null)
        {
            throw new XmlException(
                "Tried to read double value from null value");
        }
        try
        {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException e)
        {
            throw new XmlException(
                "Expected double value, found \""+value+"\"", e);
        }
    }
    
    /**
     * Parse a boolean value from the first child of the given node.
     * 
     * @param node The node
     * @return The boolean value
     * @throws XmlException If the given node was <code>null</code>
     */
    static boolean readBoolean(Node node)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read boolean value from null node");
        }
        String value = node.getFirstChild().getNodeValue();
        return Boolean.parseBoolean(value);
    }
    
    
    /**
     * Parse an enum value from the first child of the given node.
     * 
     * @param <E> The enum type
     * 
     * @param node The node
     * @param enumClass The enum class 
     * @return The enum value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * enum value could be parsed
     */
    static <E extends Enum<E>> E readEnum(Node node, Class<E> enumClass)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read "+enumClass.getSimpleName()+
                " value from null node");
        }
        String value = node.getFirstChild().getNodeValue();
        if (value == null)
        {
            throw new XmlException(
                "Tried to read "+enumClass.getSimpleName()+
                " value from null value");
        }
        try
        {
            return Enum.valueOf(enumClass, value);
        }
        catch (IllegalArgumentException e)
        {
            throw new XmlException(
                "No valid "+enumClass.getSimpleName()+
                ": \""+value+"\"");
        }
    }
    
    /**
     * Read an int value from the first child of the given node with
     * the given name. If no such child is found, then the default 
     * value will be returned.
     * 
     * @param node The node
     * @param childNodeName The child node name
     * @param defaultValue The default value
     * @return The int value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * int value could be parsed
     */
    public static int readIntChild(
        Node node, String childNodeName, int defaultValue)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read int value from child of null node");
        }
        Node child = XmlUtils.getFirstChild(node, childNodeName);
        if (child == null)
        {
            return defaultValue;
        }
        return XmlUtils.readInt(child);
    }

    /**
     * Read a double value from the first child of the given node with
     * the given name. If no such child is found, then the default 
     * value will be returned.
     * 
     * @param node The node
     * @param childNodeName The child node name
     * @param defaultValue The default value
     * @return The double value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * double value could be parsed
     */
    static double readDoubleChild(
        Node node, String childNodeName, double defaultValue)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read double value from child of null node");
        }
        Node child = XmlUtils.getFirstChild(node, childNodeName);
        if (child == null)
        {
            return defaultValue;
        }
        return XmlUtils.readDouble(child);
    }
    
    /**
     * Read an boolean value from the first child of the given node with
     * the given name. If no such child is found, then the default 
     * value will be returned.
     * 
     * @param node The node
     * @param childNodeName The child node name
     * @param defaultValue The default value
     * @return The boolean value
     * @throws XmlException If the given node was <code>null</code>
     */
    public static boolean readBooleanChild(
        Node node, String childNodeName, boolean defaultValue)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read boolean value from child of null node");
        }
        Node child = XmlUtils.getFirstChild(node, childNodeName);
        if (child == null)
        {
            return defaultValue;
        }
        return XmlUtils.readBoolean(child);
    }
    
    /**
     * Parse an enum value from the first child of the given node with
     * the given name
     * 
     * @param <E> The enum type
     * 
     * @param node The node
     * @param enumClass The enum class 
     * @param childNodeName The child node name
     * @return The enum value
     * @throws XmlException If the given node was <code>null</code>, or no 
     * enum value could be parsed
     */
    public static <E extends Enum<E>> E readEnumChild(
        Node node, Class<E> enumClass, String childNodeName)
    {
        if (node == null)
        {
            throw new XmlException(
                "Tried to read enum value from child of null node");
        }
        Node child = XmlUtils.getFirstChild(node, childNodeName);
        return XmlUtils.readEnum(child, enumClass);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private XmlUtils()
    {
        // Private constructor to prevent instantiation
    }
}
