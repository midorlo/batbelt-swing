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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Utility methods related to members.<br>
 * <br>
 * The methods in this class are mainly intended for the use as predicates
 * via method references:
 * <pre><code>
 * List&lt;Method&gt; publicStaticGetters = 
 *     Methods.getOptional(Example.class,
 *         Members::isPublic, 
 *         m -&gt; m.getName().startsWith("get"), 
 *         Members::isStatic);
 * </code></pre>
 */
public class Members
{
    /**
     * Returns whether the given member is a public instance member. 
     * 
     * @param member The member
     * @return Whether the given member is a public instance member
     */
    public static boolean isPublicInstance(Member member)
    {
        return isPublic(member) && isNotStatic(member);
    }

    /**
     * Returns whether the given member is public. 
     * 
     * @param member The member
     * @return Whether the given member is public.
     */
    public static boolean isPublic(Member member)
    {
        return Modifier.isPublic(member.getModifiers());
    }
    
    /**
     * Returns whether the given member is not public. 
     * 
     * @param member The member
     * @return Whether the given member is not public.
     */
    public static boolean isNotPublic(Member member)
    {
        return !isPublic(member);
    }
    
    /**
     * Returns whether the given member is protected. 
     * 
     * @param member The member
     * @return Whether the given member is protected.
     */
    public static boolean isProtected(Member member)
    {
        return Modifier.isProtected(member.getModifiers());
    }
    
    /**
     * Returns whether the given member is not protected. 
     * 
     * @param member The member
     * @return Whether the given member is not protected.
     */
    public static boolean isNotProtected(Member member)
    {
        return !isProtected(member);
    }
    
    /**
     * Returns whether the given member is private. 
     * 
     * @param member The member
     * @return Whether the given member is private.
     */
    public static boolean isPrivate(Member member)
    {
        return Modifier.isPrivate(member.getModifiers());
    }
    
    /**
     * Returns whether the given member is not private. 
     * 
     * @param member The member
     * @return Whether the given member is not private.
     */
    public static boolean isNotPrivate(Member member)
    {
        return !isNotPrivate(member);
    }
    
    /**
     * Returns whether the given member is package-accessible. This means 
     * that it is neither public, nor protected or private. 
     * 
     * @param member The member
     * @return Whether the given member is package-accessible.
     */
    public static boolean isPackageAccessible(Member member)
    {
        return !isPublic(member) && !isProtected(member) && !isPrivate(member);
    }
    
    /**
     * Returns whether the given member is not package-accessible. This means 
     * that it is either public, or protected or private. 
     * 
     * @param member The member
     * @return Whether the given member is not package-accessible.
     */
    public static boolean isNotPackageAccessible(Member member)
    {
        return !isPackageAccessible(member);
    }
    
    /**
     * Returns whether the given member is static. 
     * 
     * @param member The member
     * @return Whether the given member is static.
     */
    public static boolean isStatic(Member member)
    {
        return Modifier.isStatic(member.getModifiers());
    }

    /**
     * Returns whether the given member is not static. 
     * 
     * @param member The member
     * @return Whether the given member is not static.
     */
    public static boolean isNotStatic(Member member)
    {
        return !isStatic(member);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Members()
    {
        // Private constructor to prevent instantiation
    }
    
}
