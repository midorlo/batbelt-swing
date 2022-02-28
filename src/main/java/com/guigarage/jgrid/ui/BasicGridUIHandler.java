package com.guigarage.jgrid.ui;

import com.guigarage.jgrid.JGrid;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Handler for all JGrid events. The handler is added to the JGrid by the UI.
 *
 * @author hendrikebbers
 * @version $Revision$, $Date$
 */
public class BasicGridUIHandler implements PropertyChangeListener,
                                           MouseListener,
                                           KeyListener,
                                           ListSelectionListener,
                                           ListDataListener,
                                           ComponentListener,
                                           MouseMotionListener {

    private final JGrid grid;

    /**
     * Constructs a {@code BasicGridUIHandler} for a specific JGrid.
     *
     * @param grid the JGrid this {@code BasicGridUIHandler} is used for
     */
    public BasicGridUIHandler(final JGrid grid) {

        this.grid = grid;
    }


    @Override
    public void intervalAdded(final ListDataEvent e) {

        grid.getUI().markCellBoundsAsDirty();
        // TODO: Repaint everything below the new index.
        grid.repaint();
    }

    @Override
    public void intervalRemoved(final ListDataEvent e) {

        grid.getUI().markCellBoundsAsDirty();
        // TODO: Repaint everything below the deleted index.
        grid.repaint();
    }

    @Override
    public void contentsChanged(final ListDataEvent e) {

        grid.getUI().markCellBoundsAsDirty();
        // repaint all from index0 to index1
        for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
            final Rectangle cellBounds = grid.getCellBounds(i);
            if (cellBounds != null) {
                grid.repaint();
            }
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {

        grid.getUI().markCellBoundsAsDirty();
        // Only repaint the selection.
        for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
            final Rectangle cellBounds = grid.getCellBounds(i);
            if (cellBounds != null) {
                grid.repaint();
            }
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e)) {
            final int index = grid.getCellAt(e.getPoint());
            if (index >= 0) {
                grid.requestFocus();
            }
            if (isMenuShortcutKeyDown(e)) {
                if (grid.getSelectionModel().isSelectedIndex(index)) {
                    grid.getSelectionModel().removeSelectionInterval(index, index);
                } else {
                    grid.getSelectionModel().addSelectionInterval(index, index);
                }
            } else if (e.isShiftDown()) {
                final int ancestor = grid.getSelectionModel().getAnchorSelectionIndex();
                final int lead     = grid.getSelectionModel().getLeadSelectionIndex();

                if (ListSelectionUtilities.isBetweenOrEqualsLeadAndAncestor(grid.getSelectionModel(), index)) {
                    grid.getSelectionModel().removeSelectionInterval(lead, index);

                    grid.getSelectionModel().addSelectionInterval(index, index);
                    grid.getSelectionModel().setLeadSelectionIndex(index);
                } else {
                    if (!ListSelectionUtilities.isOnSameSideFromAncestorAsLead(grid.getSelectionModel(), index)) {
                        grid.getSelectionModel().removeSelectionInterval(ancestor, lead);
                        grid.getSelectionModel().addSelectionInterval(ancestor, index);
                    } else {
                        grid.getSelectionModel().addSelectionInterval(lead, index);
                    }
                }
                grid.getSelectionModel().setAnchorSelectionIndex(ancestor);
            } else {
                // clear selection to provoke a selection changed event
                grid.getSelectionModel().clearSelection();
                grid.setSelectedIndex(index);
            }
        }
        ListSelectionUtilities.refreshAnchorAndLead(grid.getSelectionModel(), grid.getModel().getSize() - 1);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        final Point anchor = e.getPoint();
        grid.setSelectionRectangleAnchor(anchor);
        grid.setSelectionRectangle(new Rectangle(anchor));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

        grid.setSelectionRectangle(null);
        grid.repaint();
    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    /**
     *
     */
    public boolean isMenuShortcutKeyDown(final InputEvent event) {

        return (event.getModifiersEx() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()) != 0;
    }

    @Override
    public void keyTyped(final KeyEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {

        final int ancestor = grid.getSelectionModel().getAnchorSelectionIndex();
        final int lead     = grid.getSelectionModel().getLeadSelectionIndex();

        if ((e.getKeyCode() == KeyEvent.VK_A) && isMenuShortcutKeyDown(e)) {
            grid.getSelectionModel().addSelectionInterval(0, grid.getModel().getSize() - 1);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            int nextIndex = grid.getSelectionModel().getLeadSelectionIndex() - 1;
            if (e.isShiftDown()) {
                if (!ListSelectionUtilities.isBetweenOrEqualsLeadAndAncestor(grid.getSelectionModel(),
                                                                             nextIndex) && (nextIndex >= 0)) {
                    while (grid.getSelectionModel().isSelectedIndex(nextIndex) && (nextIndex > 0)) {
                        nextIndex--;
                    }
                    grid.getSelectionModel().addSelectionInterval(lead, nextIndex);
                } else {
                    if (nextIndex >= 0) {
                        grid.getSelectionModel().removeSelectionInterval(lead, lead);
                        grid.getSelectionModel().setAnchorSelectionIndex(ancestor);
                        grid.getSelectionModel().setLeadSelectionIndex(nextIndex);
                    }
                }
            } else {
                if (nextIndex >= 0) {
                    grid.setSelectedIndex(nextIndex);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int nextIndex = grid.getSelectionModel().getLeadSelectionIndex() + 1;
            if (e.isShiftDown()) {
                if (!ListSelectionUtilities.isBetweenOrEqualsLeadAndAncestor(grid.getSelectionModel(),
                                                                             nextIndex) && (nextIndex < grid.getModel()
                                                                                                            .getSize())) {
                    while (grid.getSelectionModel().isSelectedIndex(nextIndex) && (nextIndex < (grid.getModel()
                                                                                                    .getSize() - 1))) {
                        nextIndex++;
                    }
                    grid.getSelectionModel().addSelectionInterval(lead, nextIndex);
                } else {
                    if (nextIndex < grid.getModel().getSize()) {
                        grid.getSelectionModel().removeSelectionInterval(lead, lead);
                        grid.getSelectionModel().setAnchorSelectionIndex(ancestor);
                        grid.getSelectionModel().setLeadSelectionIndex(nextIndex);
                    }
                }
            } else {
                if (nextIndex < grid.getModel().getSize()) {
                    grid.setSelectedIndex(nextIndex);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            final int selectedIndex = grid.getSelectionModel().getLeadSelectionIndex();
            final int row           = grid.getRowForIndex(selectedIndex);
            final int column        = grid.getColumnForIndex(selectedIndex);

            final int nextIndex = grid.getIndexAt(row + 1, column);
            if (e.isShiftDown()) {
                // TODO:Noch nicht richtig
                grid.getSelectionModel().addSelectionInterval(grid.getSelectionModel().getLeadSelectionIndex(),
                                                              nextIndex);
            } else {
                grid.setSelectedIndex(Math.min(grid.getModel().getSize() - 1, nextIndex));
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            final int selectedIndex = grid.getSelectionModel().getLeadSelectionIndex();
            final int row           = grid.getRowForIndex(selectedIndex);
            final int column        = grid.getColumnForIndex(selectedIndex);
            final int nextIndex     = grid.getIndexAt(row - 1, column);
            if (e.isShiftDown()) {
                // TODO: Noch nicht richtig
                grid.getSelectionModel().addSelectionInterval(grid.getSelectionModel().getLeadSelectionIndex(),
                                                              nextIndex);
            } else {
                grid.setSelectedIndex(Math.max(0, nextIndex));
            }
        }
        ListSelectionUtilities.refreshAnchorAndLead(grid.getSelectionModel(), grid.getModel().getSize() - 1);
    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }

    @Override
    public void mouseDragged(final MouseEvent e) {

        if (!isMenuShortcutKeyDown(e)) {
            grid.getSelectionModel().clearSelection();
        }

        final Rectangle selection = grid.getSelectionRectangle();
        final Point     anchor    = grid.getSelectionRectangleAnchor();
        selection.setBounds(Math.min(anchor.x, e.getX()),
                            Math.min(anchor.y, e.getY()),
                            Math.abs(e.getX() - anchor.x),
                            Math.abs(e.getY() - anchor.y));

        final int[] indexes = grid.getCellsIntersectedBy(selection);
        if (indexes.length > 0) {
            grid.requestFocus();
            for (final int i : indexes) {
                grid.getSelectionModel().addSelectionInterval(i, i);
            }
        }

        ListSelectionUtilities.refreshAnchorAndLead(grid.getSelectionModel(), grid.getModel().getSize() - 1);

        grid.repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {

        grid.getUI().markCellBoundsAsDirty();
    }

    @Override
    public void componentResized(final ComponentEvent e) {

        grid.getUI().markCellBoundsAsDirty();
    }

    @Override
    public void componentMoved(final ComponentEvent e) {

        grid.getUI().markCellBoundsAsDirty();
    }

    @Override
    public void componentShown(final ComponentEvent e) {

        grid.getUI().markCellBoundsAsDirty();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {

        grid.getUI().markCellBoundsAsDirty();
    }
}
