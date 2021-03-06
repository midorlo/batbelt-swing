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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility methods related to tables
 */
public class JTables
{
    /**
     * Adjust the preferred widths of the columns of the given table
     * depending on the contents of the cells and headers.
     * 
     * @param table The table to adjust
     * @param maxWidth The maximum width a column may have
     */
    public static void adjustColumnWidths(JTable table, int maxWidth)
    {
        final int safety = 20;
        for (int c = 0; c < table.getColumnCount(); c++)
        {
            TableColumn column = table.getColumnModel().getColumn(c);

            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComponent = 
                headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, 0);
            int width = headerComponent.getPreferredSize().width;

            for (int r = 0; r < table.getRowCount(); r++) 
            {
                TableCellRenderer cellRenderer = table.getCellRenderer(r, c);
                Component cellComponent = 
                    cellRenderer.getTableCellRendererComponent(
                        table, table.getValueAt(r, c), 
                        false, false, r, c);
                Dimension d = cellComponent.getPreferredSize();
                //System.out.println(
                //    "Preferred is "+d.width+" for "+cellComponent);
                width = Math.max(width, d.width);
            }
            column.setPreferredWidth(Math.min(maxWidth, width + safety));
        }
    }    
    
    /**
     * Scroll the given table so that the specified row is visible.
     * 
     * @param table The table
     * @param row The row
     */
    public static void scrollToRow(JTable table, int row)
    {
        Rectangle visibleRect = table.getVisibleRect();
        Rectangle cellRect = table.getCellRect(row, 0, true);
        Rectangle r = new Rectangle(
            visibleRect.x, cellRect.y, 
            visibleRect.width, cellRect.height);
        table.scrollRectToVisible(r);
    }
    
    /**
     * Convert all of the given model row indices to view row indices
     * for the given table
     * 
     * @param table The table
     * @param modelRows The model row indices
     * @return The view row indices
     */
    public static Set<Integer> convertRowIndicesToView(
        JTable table, Iterable<? extends Integer> modelRows)
    {
        Set<Integer> viewRows = new LinkedHashSet<Integer>();
        for (Integer modelRow : modelRows)
        {
            int viewRow = table.convertRowIndexToView(modelRow);
            viewRows.add(viewRow);
        }
        return viewRows;
    }

    /**
     * Convert all of the given view row indices to model row indices
     * for the given table
     * 
     * @param table The table
     * @param viewRows The view row indices
     * @return The model row indices
     */
    public static Set<Integer> convertRowIndicesToModel(
        JTable table, Iterable<? extends Integer> viewRows)
    {
        Set<Integer> modelRows = new LinkedHashSet<Integer>();
        for (Integer viewRow : viewRows)
        {
            int modelRow = table.convertRowIndexToModel(viewRow);
            modelRows.add(modelRow);
        }
        return modelRows;
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private JTables()
    {
        // Private constructor to prevent instantiation
    }
    
}
