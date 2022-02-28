package com.guigarage.jgrid.ui;

import com.guigarage.jgrid.JGrid;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * A basic L&F implementation of GridUI. This implementation is not static, i.e. there's one UIView implementation for
 * every JGrid objects.
 *
 * @author hendrikebbers
 * @version $Revision$, $Date$
 */
public class BasicGridUI extends GridUI {

    protected JGrid                   grid;
    private   int                     columnCount     = -1;
    private   int                     rowCount        = -1;
    private   Map<Integer, Rectangle> cellBounds;
    private   BasicGridUIHandler      handler;
    private   CellRendererPane        rendererPane;
    private   boolean                 dirtyCellBounds = true;

    @Override
    public void installUI(final JComponent c) {

        super.installUI(c);
        if (c instanceof JGrid) {
            grid = (JGrid) c;
        }
        cellBounds = new HashMap<>();

        rendererPane = new CellRendererPane();
        grid.add(rendererPane);

        handler = new BasicGridUIHandler(grid);
        grid.addMouseListener(handler);
        grid.addMouseMotionListener(handler);
        grid.addComponentListener(handler);
        grid.addKeyListener(handler);
        grid.addListDataListener(handler);
        grid.addListSelectionListener(handler);
        grid.addPropertyChangeListener(handler);
    }

    @Override
    public void uninstallUI(final JComponent c) {

        grid.remove(rendererPane);
        grid.removeMouseListener(handler);
        grid.removeMouseMotionListener(handler);
        grid.removeComponentListener(handler);
        grid.removeKeyListener(handler);
        grid.removeListDataListener(handler);
        grid.removeListSelectionListener(handler);
        grid.removePropertyChangeListener(handler);
        cellBounds.clear();
        handler      = null;
        rendererPane = null;
        cellBounds   = null;
        grid         = null;
    }

    @Override
    public void paint(final Graphics g, final JComponent c) {
        // TODO: Label mit einbauen
        maybeUpdateCellBounds();

        for (int i = 0; i < grid.getModel().getSize(); i++) {
            final int leadIndex = adjustIndex(grid.getLeadSelectionIndex(), grid);
            if (g.getClipBounds().intersects(cellBounds.get(i))) {
                paintCell(g, c, i, cellBounds.get(i), leadIndex);
                paintCellBorder(g, c, i, cellBounds.get(i), leadIndex);
            }
        }
        rendererPane.removeAll();
    }

    @Override
    public Dimension getPreferredSize(final JComponent c) {

        final int widthOneCell = grid.getHorizontalMargin() + grid.getHorizontalMargin() + grid.getFixedCellDimension();
        int width  = Math.max(c.getWidth(), widthOneCell);
        int height = getPreferredHeightForWidth(width);
        if (grid.getInsets() != null) {
            width  = grid.getInsets().left + width + grid.getInsets().right;
            height = grid.getInsets().top + height + grid.getInsets().bottom;
        }
        return new Dimension(width, height);
    }

    /**
     * Returns the preferred height of the JGrid for the specified width.
     *
     * @param width the given width
     *
     * @return the preferred height
     */
    protected int getPreferredHeightForWidth(final int width) {
        // TODO: Label mit einbauen
        int cellsInRow = 0;
        final int widthOneCell = grid.getHorizontalMargin() + grid.getHorizontalMargin() + grid.getFixedCellDimension();
        int aktWidth = width;
        if (aktWidth > widthOneCell) {
            while (aktWidth > widthOneCell) {
                aktWidth = aktWidth - widthOneCell;
                cellsInRow++;
            }
        } else {
            cellsInRow = 1;
        }
        int rows = grid.getModel().getSize() / cellsInRow;
        if ((grid.getModel().getSize() % cellsInRow) > 0) {
            rows++;
        }
        final int heightOneCell = grid.getVerticalMargin() + grid.getVerticalMargin() + grid.getFixedCellDimension();
        return rows * heightOneCell;
    }

    /**
     *
     */
    private void maybeUpdateCellBounds() {

        if (dirtyCellBounds) {
            updateCellBounds();
            grid.revalidate();
        }
    }

    /**
     * Return the index or -1 if the index is not in the range of the ListModel.
     *
     * @return the index or -1
     */
    private int adjustIndex(final int index, final JGrid grid) {

        return (index < grid.getModel().getSize())
               ? index
               : -1;
    }

    /**
     * Paints the specified cell. Subclasses should override this method and use the specified <code>Graphics</code>
     * object to render the cell.
     *
     * @param g      the <code>Graphics</code> context in which to paint
     * @param c      the JGrid being painted
     * @param index  the cellindex
     * @param bounds the bounds of the cell
     */
    protected void paintCell(final Graphics g,
                             final JComponent c,
                             final int index,
                             final Rectangle bounds,
                             final int leadIndex) {

        final boolean cellHasFocus = grid.hasFocus() && (index == leadIndex);
        final boolean isSelected   = grid.getSelectionModel().isSelectedIndex(index);

        final Object value = grid.getModel().getElementAt(index);

        final Component rendererComponent = grid.getCellRenderer(index).getGridCellRendererComponent(grid,
                                                                                                     value,
                                                                                                     index,
                                                                                                     isSelected,
                                                                                                     cellHasFocus);
        rendererPane.paintComponent(g, rendererComponent, grid, bounds.x, bounds.y, bounds.width, bounds.height, true);
    }

    /**
     * Paints the Border for the specified cell. Subclasses should override this method and use the specified <code>
     * Graphics</code> object to render the Border of the cell.
     *
     * @param g      the <code>Graphics</code> context in which to paint
     * @param c      the JGrid being painted
     * @param index  the cellindex
     * @param bounds the bounds of the cell
     */
    protected void paintCellBorder(final Graphics g,
                                   final JComponent c,
                                   final int index,
                                   final Rectangle bounds,
                                   final int leadIndex) {

    }

    /**
     *
     */
    private void updateCellBounds() {

        cellBounds.clear();
        int       x          = 0;
        int       y          = grid.getVerticalMargin() + grid.getInsets().top;
        int       row        = 0;
        int       indexInRow = 0;
        final int startX     = calcStartX();
        x = startX + grid.getInsets().left;
        for (int i = 0; i < grid.getModel().getSize(); i++) {
            if (((x + grid.getHorizontalMargin() + grid.getHorizontalMargin() + grid.getFixedCellDimension()) > grid.getWidth()) && (indexInRow > 0)) {
                columnCount = indexInRow;
                row++;
                indexInRow = 0;
                x          = startX;
                y          = y + grid.getVerticalMargin() + grid.getVerticalMargin() + grid.getFixedCellDimension();
            }
            indexInRow++;

            x = x + grid.getHorizontalMargin();
            final Rectangle r = new Rectangle(x, y, grid.getFixedCellDimension(), grid.getFixedCellDimension());
            x = x + grid.getHorizontalMargin() + grid.getFixedCellDimension();

            cellBounds.put(i, r);
        }
        rowCount        = row + 1;
        dirtyCellBounds = false;
    }

    /**
     *
     */
    private int calcStartX() {
        // Damit Zentriert, wird Start-X abhängig von breite gesetzt
        // TODO: grid.INSETS beachten!!!!!
        final int widthOneCell = grid.getHorizontalMargin() + grid.getHorizontalMargin() + grid.getFixedCellDimension();
        int aktWidth = grid.getWidth();
        int startX   = 0;
        if (grid.getHorizontalAlignment() == SwingConstants.CENTER) {
            if (aktWidth > widthOneCell) {
                while (aktWidth > widthOneCell) {
                    aktWidth = aktWidth - widthOneCell;
                }
                startX = aktWidth / 2;
            }
        } else if (grid.getHorizontalAlignment() == SwingConstants.RIGHT) {
            while (aktWidth > widthOneCell) {
                aktWidth = aktWidth - widthOneCell;
                startX   = aktWidth;
            }
        }
        return startX;
    }

    /**
     * Helper for debbuging. Returns true if systemproperty "jgrid.debug" is set to "true"
     *
     * @return true if debugMode is active
     */
    protected boolean isDebugMode() {

        return "true".equals(System.getProperty("jgrid.debug", "false"));
    }

    @Override
    public int[] getCellsIntersectedBy(final Rectangle rect) {

        final Set<Integer> intersectSet = new TreeSet<>();

        for (final Entry<Integer, Rectangle> nextCellBounds : cellBounds.entrySet()) {
            if (nextCellBounds.getValue().intersects(rect)) {
                intersectSet.add(nextCellBounds.getKey());
            }
        }

        final int[] returnArray = new int[intersectSet.size()];
        int         i           = 0;
        for (final int nextInt : intersectSet) {
            returnArray[i++] = nextInt;
        }
        return returnArray;
    }

    @Override
    public int getCellAt(final Point point) {

        maybeUpdateCellBounds();
        for (final Entry<Integer, Rectangle> entry : cellBounds.entrySet()) {
            if (entry.getValue().contains(point)) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    @Override
    public Rectangle getCellBounds(final int index) {

        maybeUpdateCellBounds();
        return cellBounds.get(index);
    }

    @Override
    public int getColumnCount() {

        maybeUpdateCellBounds();
        return columnCount;
    }

    @Override
    public int getIndexAt(final int row, final int column) {

        if ((row < 0) || (column < 0)) {
            return -1;
        }
        int index = 0;
        if (row > 0) {
            index = row * getColumnCount();
        }
        return index + column;
    }

    @Override
    public int getRowCount() {

        maybeUpdateCellBounds();
        return rowCount;
    }

    @Override
    public int getRowForIndex(final int selectedIndex) {

        maybeUpdateCellBounds();
        return selectedIndex / columnCount;
    }

    @Override
    public int getColumnForIndex(final int selectedIndex) {

        final int row = getRowForIndex(selectedIndex);
        if (row == 0) {
            return selectedIndex;
        }
        final int prev  = (row * getColumnCount());
        final int index = selectedIndex - prev;
        return index;
    }

    @Override
    public void markCellBoundsAsDirty() {

        dirtyCellBounds = true;
    }
}
