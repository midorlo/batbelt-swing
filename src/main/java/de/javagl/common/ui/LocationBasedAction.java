/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
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
package de.javagl.common.ui;

import javax.swing.*;
import java.awt.*;

/**
 * An AbstractAction that will be notified about the component 
 * and the location where a popup menu will be shown when it
 * is contained in a popup menu that is triggered by a 
 * {@link LocationBasedPopupHandler}.
 */
public abstract class LocationBasedAction extends AbstractAction
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6112032299610132765L;

    /**
     * Will be called by the {@link LocationBasedPopupHandler} 
     * before the popup menu is shown at the specified position
     * on the given component. The default implementation is
     * empty and may be overridden to configure this action 
     * based on the given parameters.
     * 
     * @param component The component
     * @param x The x-position 
     * @param y The y-position
     */
    protected void prepareShow(Component component, int x, int y)
    {
        // Empty default implementation
    }
}