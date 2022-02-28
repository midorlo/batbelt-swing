package de.javagl.autogui.model.properties;


import de.javagl.reflection.Methods;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods related to Java Beans
 */
public class BeanUtils
{
    /**
     * Returns the BeanInfo for the given bean class. Returns <code>null</code>
     * if an IntrospectionException is caused
     *
     * @param beanClass The bean class
     * @return The BeanInfo
     */
    private static BeanInfo getBeanInfoOptional(Class<?> beanClass)
    {
        try
        {
            return Introspector.getBeanInfo(beanClass);
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    /**
     * Returns an unmodifiable list containing the PropertyDescriptors of
     * the given bean class. Returns an empty list if either the BeanInfo
     * or the PropertyDescriptors could not be obtained.
     *
     * @param beanClass The bean class
     * @return The PropertyDescriptors
     */
    private static List<PropertyDescriptor> getPropertyDescriptorsOptional(
            Class<?> beanClass)
    {
        BeanInfo beanInfo = getBeanInfoOptional(beanClass);
        if (beanInfo == null)
        {
            return Collections.emptyList();
        }
        PropertyDescriptor propertyDescriptors[] =
                beanInfo.getPropertyDescriptors();
        if (propertyDescriptors == null)
        {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(
                new ArrayList<PropertyDescriptor>(
                        Arrays.asList(propertyDescriptors)));
    }


    /**
     * Returns the PropertyDescriptor with the given name from the given
     * bean class, or <code>null</code> if there is no such property
     *
     * @param beanClass The bean class
     * @param propertyName The property name
     * @return The PropertyDescriptor
     */
    private static PropertyDescriptor getPropertyDescriptorOptional(
            Class<?> beanClass, String propertyName)
    {
        List<PropertyDescriptor> propertyDescriptors =
                getPropertyDescriptorsOptional(beanClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            if (propertyDescriptor.getName().equals(propertyName))
            {
                return propertyDescriptor;
            }
        }
        return null;
    }


    /**
     * Set all properties of the target bean to the values obtained
     * from the source bean. <br>
     * <br>
     * This method will extract all properties of the target bean class
     * using {@link #getMutablePropertyNamesOptional(Class)}. For each
     * property, the corresponding read-method of the source bean class
     * will be called to obtain the value. This value will then be passed
     * to the target bean by calling the write-method for the property.<br>
     * <br>
     * Any checked exception that may be thrown internally will silently
     * be ignored.
     *
     * @param targetBean The target bean
     * @param sourceBean The source bean
     */
    public static void setAllOptional(Object targetBean, Object sourceBean)
    {
        Class<?> sourceClass = targetBean.getClass();
        Class<?> targetClass = targetBean.getClass();
        List<String> propertyNames =
                getMutablePropertyNamesOptional(targetClass);
        for (String propertyName : propertyNames)
        {
            Method readMethod =
                    getReadMethodOptional(sourceClass, propertyName);
            Method writeMethod =
                    getWriteMethodOptional(targetClass, propertyName);
            if (readMethod != null && writeMethod != null)
            {
                try
                {
                    Object value = readMethod.invoke(sourceBean);
                    Methods.invokeOptional(writeMethod, targetBean, value);
                }
                catch (IllegalArgumentException e)
                {
                    // Ignore
                }
                catch (IllegalAccessException e)
                {
                    // Ignore
                }
                catch (InvocationTargetException e)
                {
                    // Ignore
                }
                catch (SecurityException e)
                {
                    // Ignore
                }
            }
        }
    }


    /**
     * Converts the given property name into a description. This
     * is done by converting the first letter to upper case, and
     * inserting a space before each other upper case letter.
     * For example, the propertyName <code>inputFileName</code> will
     * be converted into the String <code>Input File Name</code>.
     *
     * @param propertyName The property name
     * @return The description string for the property name.
     */
    public static String getDescription(String propertyName)
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<propertyName.length(); i++)
        {
            char c = propertyName.charAt(i);
            if (Character.isUpperCase(c))
            {
                sb.append(" ");
            }
            else if (i == 0)
            {
                c = Character.toUpperCase(c);
            }
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * Invokes the write-method on the given object for the property with
     * the given name, passing in the given value.<br>
     * <br>
     * Any checked exception that may be thrown internally will silently
     * be ignored.
     *
     * @param bean The object to call the method on
     * @param propertyName The property name
     * @param value The value to pass to the method
     */
    public static void invokeWriteMethodOptional(
            Object bean, String propertyName, Object value)
    {
        Class<?> c = bean.getClass();
        Method method = getWriteMethodOptional(c, propertyName);
        if (method != null)
        {
            Methods.invokeOptional(method, bean, value);
        }
    }

    /**
     * Invokes the read method for the property with the given name on
     * the given object, and returns the result.<br>
     * <br>
     * Any checked exception that may be thrown internally will silently
     * be ignored, and <code>null</code> will be returned in this case.
     *
     * @param bean The object to invoke the method on
     * @param propertyName The name of the property to query
     * @return The result of the method invocation
     */
    public static Object invokeReadMethodOptional(
            Object bean, String propertyName)
    {
        Class<?> c = bean.getClass();
        Method method = getReadMethodOptional(c, propertyName);
        if (method == null)
        {
            return null;
        }
        return Methods.invokeOptional(method, bean);
    }



    /**
     * Returns an unmodifiable list of all property names of the given bean
     * class for which a read method and a write method exists. If the bean
     * class can not be introspected, an empty list will be returned.
     *
     * @param beanClass The bean class
     * @return The property names
     */
    public static List<String> getMutablePropertyNamesOptional(
            Class<?> beanClass)
    {
        List<PropertyDescriptor> propertyDescriptors =
                getPropertyDescriptorsOptional(beanClass);
        List<String> result = new ArrayList<String>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            String propertyName = propertyDescriptor.getName();
            Method readMethod =
                    getReadMethodOptional(beanClass, propertyName);
            Method writeMethod =
                    getWriteMethodOptional(beanClass, propertyName);
            if (readMethod != null && writeMethod != null)
            {
                result.add(propertyName);
            }
        }
        return Collections.unmodifiableList(result);
    }


    /**
     * Returns the type of the property with the given name in the given
     * bean class. Returns <code>null</code> if there is no such property,
     * or the property is an indexed property that does not allow non-indexed
     * access.
     *
     * @param beanClass The bean class
     * @param propertyName The property name
     * @return The type of the property, or <code>null</code> if there
     * is no such property.
     */
    public static Class<?> getPropertyTypeOptional(
            Class<?> beanClass, String propertyName)
    {
        PropertyDescriptor propertyDescriptor =
                getPropertyDescriptorOptional(beanClass, propertyName);
        if (propertyDescriptor == null)
        {
            return null;
        }
        return propertyDescriptor.getPropertyType();
    }




    /**
     * Returns the write method for the property with the given name
     * in the given bean class.
     *
     * @param beanClass The bean class
     * @param propertyName The property name
     * @return The write method, or <code>null</code> if no appropriate
     * write method is found.
     */
    public static Method getWriteMethodOptional(
            Class<?> beanClass, String propertyName)
    {
        PropertyDescriptor propertyDescriptor =
                getPropertyDescriptorOptional(beanClass, propertyName);
        if (propertyDescriptor == null)
        {
            return null;
        }
        return propertyDescriptor.getWriteMethod();
    }

    /**
     * Returns the read method for the property with the given name in the
     * given bean class.
     *
     * @param beanClass The bean class
     * @param propertyName The property name
     * @return The read method for the property, or <code>null</code>
     * if no appropriate read method is found.
     */
    public static Method getReadMethodOptional(
            Class<?> beanClass, String propertyName)
    {
        PropertyDescriptor propertyDescriptor =
                getPropertyDescriptorOptional(beanClass, propertyName);
        if (propertyDescriptor == null)
        {
            return null;
        }
        return propertyDescriptor.getReadMethod();
    }



    /**
     * Private constructor to prevent instantiation
     */
    private BeanUtils()
    {
        // Private constructor to prevent instantiation
    }

}

