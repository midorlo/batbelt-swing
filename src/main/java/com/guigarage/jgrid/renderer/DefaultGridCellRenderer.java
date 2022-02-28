package com.guigarage.jgrid.renderer;

import com.guigarage.jgrid.JGrid;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * The default CellRenderer for JGrid. Works like DefaultListRenderer.
 *
 * @author Hendrik Ebbers
 * @version 0.1
 * @see DefaultListRenderer
 * @since 0.1
 */
public class DefaultGridCellRenderer extends JLabel implements GridCellRenderer {

    /**
     * Creates a new DefaultGridCellRenderer object.
     */
    public DefaultGridCellRenderer() {

        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(CENTER);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }
    @Override
    public Component getGridCellRendererComponent(final JGrid grid,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean cellHasFocus) {

        if (isSelected) {
            setBorder(new BevelBorder(BevelBorder.LOWERED));
            setBackground(grid.getSelectionBackground());
            setForeground(grid.getSelectionForeground());
        } else {
            setBorder(new BevelBorder(BevelBorder.RAISED));
            setBackground(grid.getBackground());
            setForeground(grid.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            setText((value == null)
                    ? ""
                    : value.toString());
        }

        setEnabled(grid.isEnabled());
        setFont(grid.getFont());
        return this;
    }

    @Override
    public String getToolTipText() {

        return getText();
    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final char oldValue, final char newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint(final long tm, final int x, final int y, final int width, final int height) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint(final Rectangle r) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void revalidate() {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void invalidate() {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void validate() {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void repaint() {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final byte oldValue, final byte newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final short oldValue, final short newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final long oldValue, final long newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final float oldValue, final float newValue) {

    }

    /**
     * Overridden for performance reasons.
     */
    @Override
    public void firePropertyChange(final String propertyName, final double oldValue, final double newValue) {

    }
}
