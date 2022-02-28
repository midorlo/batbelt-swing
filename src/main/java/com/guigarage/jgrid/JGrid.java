package com.guigarage.jgrid;

import com.guigarage.jgrid.eventproxies.ListDataProxy;
import com.guigarage.jgrid.eventproxies.ListSelectionProxy;
import com.guigarage.jgrid.renderer.GridCellRenderer;
import com.guigarage.jgrid.renderer.GridCellRendererManager;
import com.guigarage.jgrid.ui.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

@SuppressWarnings("unused")
public class JGrid extends JComponent implements Scrollable,
                                                 SwingConstants {

    private static final String                  uiClassID           = "GridUI";
    private final        ListSelectionProxy      selectionProxy      = new ListSelectionProxy();
    private final        ListDataProxy           dataProxy           = new ListDataProxy();
    private              ListSelectionModel      selectionModel;
    private              ListModel<?>            model;
    private              GridCellRendererManager cellRendererManager;
    private              int                     fixedCellDimension  = 128;
    private              int                     horizontalMargin    = 16;
    private              int                     verticalMargin      = 16;
    private              Color                   selectionForeground;
    private              Color                   selectionBorderColor;
    private              Color                   selectionBackground;
    private              Color                   cellBackground;
    private              int                     horizontalAlignment = CENTER;
    private              Rectangle               selectionRectangle;
    private              Point                   selectionRectangleAnchor;

    //<editor-fold desc="Constructors">

    /**
     * Constructs a {@code JGrid} with a DefaultListModel.
     *
     * @since 0.1
     */
    public JGrid() {

        this(new DefaultListModel<>());
    }

    /**
     * Constructs a {@code JGrid} that displays elements from the specified, {@code non-null}, model. All {@code JGrid}
     * constructors must delegate to this one.
     *
     * @param model the model for the grid
     *
     * @throws IllegalArgumentException if the model is {@code null}
     * @since 0.1
     */
    public JGrid(final ListModel<?> model) throws IllegalArgumentException {

        if (model == null) {
            throw new IllegalArgumentException("dataModel must be non null");
        }

        final ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.registerComponent(this);

        setSelectionModel(createDefaultSelectionModel());
        setModel(model);
        setAutoscrolls(true);
        setOpaque(true);
        setCellRendererManager(new GridCellRendererManager());
        setUI(new MacOsGridUI());
        updateUI();
    }
    //</editor-fold>


    /**
     * Returns a default instance of {@code ListSelectionModel}; called during construction to initialize the grids
     * selectionModel. Normally this returns a {@code DefaultListSelectionModel}
     *
     * @return a default ListSelectionModel
     *
     * @see JList
     * @since 0.1
     */
    protected ListSelectionModel createDefaultSelectionModel() {

        return new DefaultListSelectionModel();
    }

    @Override
    public void updateUI() {

        cellRendererManager.updateRendererUI();
    }

    /**
     * Calls revalidate() and repaint().
     */
    protected void revalidateAndRepaint() {

        revalidate();
        repaint();
    }

    /**
     * Returns the UI-Class for this JComponent. This is always a GridUI
     *
     * @return the UIClass
     *
     * @since 0.1
     */
    public GridUI getUI() {

        return (GridUI) ui;
    }

    @Override
    public String getUIClassID() {

        return uiClassID;
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);
        if (selectionRectangle != null) {
            final Graphics2D g2 = (Graphics2D) g;

            g2.setColor(this.getSelectionBackground());
            g2.setStroke(new BasicStroke(3));
            g2.draw(selectionRectangle);

            g2.setColor(this.getSelectionBorderColor());
            g2.setStroke(new BasicStroke(1));
            g2.draw(selectionRectangle);

            g2.setColor(new Color(
                    selectionBorderColor.getRed(),
                    selectionBorderColor.getGreen(),
                    selectionBorderColor.getBlue(),
                    128));
            g2.fill(selectionRectangle);
        }
    }

    /**
     * Getter for the selectionBackground. Each selected Cell paints the background in this Color. That means the
     * background-Property in the renderer is set to the selection-background.
     *
     * @return the selectionBackground
     *
     * @since 0.1
     */
    public Color getSelectionBackground() {

        return selectionBackground;
    }

    /**
     * Getter for the selectionBorderColor. The UIClass can use this for border-painting.
     *
     * @return the selectionBorderColor
     *
     * @since 0.1
     */
    public Color getSelectionBorderColor() {

        return selectionBorderColor;
    }

    /**
     * Setter for the selectionBorderColor. The UIClass can use this for border-painting. Fires <code>
     * PropertyChangeEvent</code> with the <code>selectionBorderColor</code> property
     *
     * @param selectionBorderColor the new selectionForeground
     *
     * @since 0.1
     */
    public void setSelectionBorderColor(final Color selectionBorderColor) {

        final Color oldValue = this.selectionBorderColor;
        this.selectionBorderColor = selectionBorderColor;
        firePropertyChange("selectionBorderColor", oldValue,
                           selectionBorderColor);
        repaint();
    }

    /**
     * Setter for the selectionBackground. Each selected Cell paints the background in this Color. That means the
     * background-Property in the renderer is set to the selection-background. Fires <code>PropertyChangeEvent</code>
     * with the <code>selectionBackground</code> property
     *
     * @param selectionBackground the new selectionBackground
     *
     * @since 0.1
     */
    public void setSelectionBackground(final Color selectionBackground) {

        final Color oldValue = this.selectionBackground;
        this.selectionBackground = selectionBackground;
        firePropertyChange("selectionBackground", oldValue, selectionBackground);
        repaint();
    }

    @Override
    public String getToolTipText(final MouseEvent event) {

        if (event != null) {
            final Point p     = event.getPoint();
            final int   index = getCellAt(p);

            if (index >= 0) {
                final Rectangle cellBounds = getCellBounds(index);
                if ((cellBounds != null) && cellBounds.contains(p.x, p.y)) {
                    final Component renderer = getCellRenderer(index).getGridCellRendererComponent(
                            this,
                            getModel().getElementAt(index),
                            index,
                            getSelectionModel().isSelectedIndex(index),
                            hasFocus()
                            && (getSelectionModel().getLeadSelectionIndex() == index));
                    if (renderer instanceof JComponent) {
                        //noinspection deprecation
                        return ((JComponent) renderer).getToolTipText(new MouseEvent(
                                renderer,
                                event.getID(),
                                event.getWhen(),
                                event.getModifiers(),
                                p.x
                                - cellBounds.x,
                                p.y
                                - cellBounds.y,
                                event.getXOnScreen(),
                                event.getYOnScreen(),
                                event.getClickCount(),
                                event.isPopupTrigger(),
                                event.getButton()));
                    }
                }
            }
        }
        return super.getToolTipText();
    }

    /**
     * Returns the index of the cell at the given point. Returns -1 if no cell is at this point
     *
     * @param point the pint in the grid
     *
     * @return the index of the cell at the point
     *
     * @see BasicGridUI
     * @since 0.1
     */
    public int getCellAt(final Point point) {

        return getUI().getCellAt(point);
    }

    /**
     * Returns the bounds inside the grid for the cell at index.
     *
     * @param index the index of the cell
     *
     * @return the cell bounds
     *
     * @since 0.1
     */
    public Rectangle getCellBounds(final int index) {

        return getUI().getCellBounds(index);
    }

    /**
     * Returns the GridCellRenderer for a specific cell.
     *
     * @param index the index of the cell
     *
     * @return the GridCellRenderer
     *
     * @see JGrid#getCellRendererManager()
     * @see #setCellRendererManager(GridCellRendererManager)
     * @see GridCellRendererManager
     * @since 0.3
     */
    public GridCellRenderer getCellRenderer(final int index) {

        return cellRendererManager.getRendererForClass(getModel().getElementAt(
                index).getClass());
    }

    /**
     * Return the Model of the JGrid.
     *
     * @return The Model of the JGrid
     *
     * @see JList
     * @since 0.1
     */
    public ListModel<?> getModel() {

        return model;
    }

    /**
     * Returns the ListSelectionModel.
     *
     * @return the ListSelectionModel
     *
     * @see JList
     * @since 0.1
     */
    public ListSelectionModel getSelectionModel() {

        return selectionModel;
    }

    /**
     * Sets the {@code ListSelectionModel} of the JGrid.
     *
     * @param selectionModel the new {@code ListSelectionModel}
     *
     * @throws IllegalArgumentException if the model is null
     * @see #getSelectionModel()
     */
    public void setSelectionModel(final ListSelectionModel selectionModel) {

        if (selectionModel == null) {
            throw new IllegalArgumentException(
                    "selectionModel must be non null");
        }
        if (this.selectionModel != null) {
            this.selectionModel.removeListSelectionListener(selectionProxy);
        }
        this.selectionModel = selectionModel;
        this.selectionModel.addListSelectionListener(selectionProxy);
    }

    /**
     * Sets the model that represents values of the JGrid.
     *
     * @param model the new {@code ListModel}
     *
     * @throws IllegalArgumentException if the model is null
     * @see #getModel
     */
    public void setModel(final ListModel<?> model) throws IllegalArgumentException {

        if (model == null) {
            throw new IllegalArgumentException("model must be non null");
        }
        final ListModel<?> oldModel = this.model;
        if (oldModel != null) {
            oldModel.removeListDataListener(dataProxy);
        }
        this.model = model;
        this.model.addListDataListener(dataProxy);

        firePropertyChange("model", oldModel, this.model);
        selectionModel.clearSelection();
    }

    /**
     * Setter for the UIClass.
     *
     * @param ui the new UI
     *
     * @since 0.1
     */
    public void setUI(final BasicGridUI ui) {

        super.setUI(ui);
    }

    /**
     * Adds a listener to the JGrid, to be notified each time a change to the selection occurs. This Method uses a
     * {@code ListSelectionProxy}. Every time the underlying {@code ListSelectionModel} changes all listeners will be
     * registered to the new model and registered from the old one. For adding listeners to the model-scope (direct
     * to the model) use ListSelectionListener.addListSelectionListener
     *
     * @param l listener the {@code ListSelectionListener} to add
     *
     * @see #removeListSelectionListener
     */
    public void addListSelectionListener(final ListSelectionListener l) {

        selectionProxy.addListSelectionListener(l);
    }

    /**
     * Removes a listener from the JGrid.
     *
     * @param l listener the {@code ListSelectionListener} to remove
     *
     * @see #addListSelectionListener
     */
    public void removeListSelectionListener(final ListSelectionListener l) {

        selectionProxy.removeListSelectionListener(l);
    }

    /**
     * Adds a listener to the JGrid, to be notified each time a change to the data occurs. This Method uses a
     * {@code ListDataProxy}. Every time the underlying {@code ListModel} changes all listeners will be registered to
     * the new model and registered from the old one. For adding listeners to the model-scope (direct to the model)
     * use ListModel.addListDataListener
     *
     * @param l listener the {@code ListSelectionListener} to add
     *
     * @see #removeListDataListener
     */
    public void addListDataListener(final ListDataListener l) {

        dataProxy.addListDataListener(l);
    }

    /**
     * Removes a listener from the JGrid.
     *
     * @param l listener the {@code ListDataListener} to remove
     *
     * @see #addListDataListener
     */
    public void removeListDataListener(final ListDataListener l) {

        dataProxy.addListDataListener(l);
    }

    /**
     * Return the {@code GridCellRendererManager} of the JGrid.
     *
     * @return The {@code GridCellRendererManager} of the JGrid
     *
     * @see GridCellRendererManager
     * @since 0.3
     */
    public GridCellRendererManager getCellRendererManager() {

        return cellRendererManager;
    }

    /**
     * Sets the {@code GridCellRendererManager} of the JGrid.
     *
     * @param cellRendererManager the new {@code GridCellRendererManager}
     *
     * @throws IllegalArgumentException if the cellRendererManager is null
     * @see GridCellRendererManager
     * @see #getCellRendererManager()
     * @since 0.3
     */
    public void setCellRendererManager(final GridCellRendererManager cellRendererManager)
            throws IllegalArgumentException {

        if (cellRendererManager == null) {
            throw new IllegalArgumentException(
                    "cellRendererManager must be non null");
        }

        final GridCellRendererManager oldManager = this.cellRendererManager;

        this.cellRendererManager = cellRendererManager;
        cellRendererManager.updateRendererUI();

        firePropertyChange("cellRendererManager", oldManager,
                           this.cellRendererManager);

        revalidateAndRepaint();
    }

    /**
     * Returns the selected Index from the ListSelectionModel.
     *
     * @return the selected Index
     *
     * @see ListSelectionModel
     * @since 0.1
     */
    public int getSelectedIndex() {

        return getSelectionModel().getMinSelectionIndex();
    }

    /**
     * Sets the index of the selected cell.
     *
     * @param index the index of the selected cell
     *
     * @since 0.1
     */
    public void setSelectedIndex(final int index) {

        if (index >= getModel().getSize()) {
            return;
        }
        getSelectionModel().setSelectionInterval(index, index);
    }

    /**
     * Returns the leadSelectionIndex from the ListSelectionModel.
     *
     * @return the leadSelectionIndex
     *
     * @see ListSelectionModel
     * @since 0.1
     */
    public int getLeadSelectionIndex() {

        return getSelectionModel().getLeadSelectionIndex();
    }

    /**

     *

     */
    public int[] getSelectedIndices() {

        final ListSelectionModel sm   = getSelectionModel();
        final int                iMin = sm.getMinSelectionIndex();
        final int                iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new int[0];
        }

        final int[] rvTmp = new int[1 + (iMax - iMin)];
        int         n     = 0;
        for (int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = i;
            }
        }
        final int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }

    /**

     *

     */
    public List<?> getSelectedValuesList() {

        final ListSelectionModel sm = getSelectionModel();
        final ListModel<?>       dm = getModel();

        final int iMin = sm.getMinSelectionIndex();
        final int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return Collections.emptyList();
        }

        final List<Object> selectedItems = new ArrayList<>();
        for (int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                Object elementAt = dm.getElementAt(i);
                selectedItems.add(elementAt);
            }
        }
        return selectedItems;
    }

    /**

     *
     * @param index
     */
    public void ensureIndexIsVisible(final int index) {

        final Rectangle cellBounds = getCellBounds(index);
        if (cellBounds != null) {
            scrollRectToVisible(cellBounds);
        }
    }

    /**
     * Returns the horizontalMargin. The horizontalMargin is the horizontal space between two cells
     *
     * @return the horizontalMargin
     *
     * @since 0.1
     */
    public int getHorizontalMargin() {

        return horizontalMargin;
    }

    /**
     * Sets the horizontal margin between all elements in the grid. Fires <code>PropertyChangeEvent</code> with the
     * <code>horizontalMargin</code> property
     *
     * @param horizontalMargin the horizontal margin for this Grid
     *
     * @since 0.1
     */
    public void setHorizontalMargin(final int horizontalMargin) {

        this.horizontalMargin = horizontalMargin;
        firePropertyChange("horizontalMargin", horizontalMargin, horizontalMargin);
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {

        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {

        return getVerticalMargin() + getVerticalMargin()
               + getFixedCellDimension();
    }

    @Override
    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {

        return getVerticalMargin() + getVerticalMargin()
               + getFixedCellDimension();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {

        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {

        if (getParent() instanceof JViewport) {
            return (getParent().getHeight() > getPreferredSize().height);
        }
        return false;
    }

    /**
     * Returns the verticalMargin. The verticalMargin is the vertical space between two cells
     *
     * @return the verticalMargin
     *
     * @since 0.1
     */
    public int getVerticalMargin() {

        return verticalMargin;
    }

    /**
     * Sets the vertical margin between all elements in the grid. Fires <code>PropertyChangeEvent</code> with the <code>
     * verticalMargin</code> property
     *
     * @param verticalMargin the vertical margin for this Grid
     *
     * @since 0.1
     */
    public void setVerticalMargin(final int verticalMargin) {

        this.verticalMargin = verticalMargin;
        firePropertyChange("verticalMargin", verticalMargin, verticalMargin);
        revalidate();
        repaint();
    }

    /**
     * Returns the Dimension of a cell. Each cell in the Grid has a width and height equals the fixedCellDimension
     *
     * @return the fixedCellDimension
     *
     * @since 0.1
     */
    public int getFixedCellDimension() {

        return fixedCellDimension;
    }

    /**
     * Sets the rendering dimension for all elements in the grid. The width and height of each element is equals the
     * dimension. Fires <code>PropertyChangeEvent</code> with the <code>fixedCellDimension</code> property
     *
     * @param dimension the new dimension for this Grid
     *
     * @since 0.1
     */
    public void setFixedCellDimension(final int dimension) {

        final int oldValue = fixedCellDimension;
        fixedCellDimension = dimension;
        firePropertyChange("fixedCellDimension", oldValue, fixedCellDimension);
        revalidate();
        repaint();
    }

    /**
     * Getter for the selectionForeground. Each selected Cell paints the foreground in this Color. That means the
     * foreground-Property in the renderer is set to the selectionForeground.
     *
     * @return the selectionForeground
     *
     * @since 0.1
     */
    public Color getSelectionForeground() {

        return selectionForeground;
    }

    /**
     * Setter for the selectionForeground. Each selected Cell paints the foreground in this Color. That means the
     * foreground-Property in the renderer is set to the selectionForeground. Fires <code>PropertyChangeEvent</code>
     * with the <code>selectionForeground</code> property
     *
     * @param selectionForeground the new selectionForeground
     *
     * @since 0.1
     */
    public void setSelectionForeground(final Color selectionForeground) {

        final Color oldValue = this.selectionForeground;
        this.selectionForeground = selectionForeground;
        firePropertyChange("selectionForeground", oldValue, selectionForeground);
        repaint();
    }

    /**
     * Getter for the default background. Each Cell paints the background in this Color. That means the
     * background-Property in the renderer is set to the cellBackground.
     *
     * @return the default background
     *
     * @since 0.1
     */
    public Color getCellBackground() {

        return cellBackground;
    }

    /**
     * Setter for the default background. Each Cell paints the background in this Color. That means the
     * background-Property in the renderer is set to the cellBackground. Fires <code>PropertyChangeEvent</code> with the
     * <code>cellBackground</code> property
     *
     * @param cellBackground the new default background
     *
     * @since 0.1
     */
    public void setCellBackground(final Color cellBackground) {

        final Color oldValue = this.cellBackground;
        this.cellBackground = cellBackground;
        firePropertyChange("cellBackground", oldValue, cellBackground);
        repaint();
    }

    /**

     *
     * @param rectangle
     *

     */
    public int[] getCellsIntersectedBy(final Rectangle rectangle) {

        return getUI().getCellsIntersectedBy(rectangle);
    }

    /**
     * Getter for the horizontal alignment. <code>LEFT</code> / <code>CENTER</code> / <code>RIGHT</code> / <code>
     * LEADING</code> & <code>TRAILING</code> allowed
     *
     * @return the horizontal alignment
     *
     * @since 0.1
     */
    public int getHorizontalAlignment() {

        return horizontalAlignment;
    }

    /**
     * Setter for the horizontal alignment. <code>LEFT</code> / <code>CENTER</code> / <code>RIGHT</code> / <code>
     * LEADING</code> & <code>TRAILING</code> allowed Fires <code>PropertyChangeEvent</code> with the <code>
     * horizontalAlignment</code> property
     *
     * @param alignment the new horizontal alignment
     *
     * @throws IllegalArgumentException if <code>alignment</code> not <code>LEFT</code> / <code>CENTER</code> /
     *                                  <code>RIGHT</code> / <code>LEADING</code> & <code>TRAILING</code>
     * @since 0.1
     */
    public void setHorizontalAlignment(final int alignment) {

        if ((alignment == LEFT) || (alignment == CENTER)
            || (alignment == RIGHT)
            || (alignment == LEADING)
            || (alignment == TRAILING)) {
            if (alignment == horizontalAlignment) {
                return;
            }
            final int oldValue = horizontalAlignment;
            horizontalAlignment = alignment;
            firePropertyChange("horizontalAlignment", oldValue,
                               horizontalAlignment);
            repaint();
        } else {
            throw new IllegalArgumentException("Illegal HorizontalAlignment: "
                                               + alignment);
        }
    }

    /**
     * Returns the model-index of the cell at <code>row</code> / <code>column.</code>
     *
     * @param row    the row of the cell
     * @param column the column of the cell
     *
     * @return model-index of the cell
     */
    public int getIndexAt(final int row, final int column) {

        return getUI().getIndexAt(row, column);
    }

    /**
     * Returns the index of the column where <code>modelIndex</code> is in.
     *
     * @param index selectedIndex the model-index
     *
     * @return index of the column
     */
    public int getColumnForIndex(final int index) {

        return getUI().getColumnForIndex(index);
    }

    /**
     * Returns the index of the row where <code>modelIndex</code> is in.
     *
     * @param index selectedIndex the model-index
     *
     * @return index of the row
     */
    public int getRowForIndex(final int index) {

        return getUI().getRowForIndex(index);
    }

    /**

     *

     */
    public Rectangle getSelectionRectangle() {

        return selectionRectangle;
    }

    /**
     *
     * @param selectionRectangle
     */
    public void setSelectionRectangle(final Rectangle selectionRectangle) {

        this.selectionRectangle = selectionRectangle;
    }

    /**

     *

     */
    public Point getSelectionRectangleAnchor() {

        return selectionRectangleAnchor;
    }

    /**

     *
     * @param selectionRectangleAnchor
     */
    public void setSelectionRectangleAnchor(final Point selectionRectangleAnchor) {

        this.selectionRectangleAnchor = selectionRectangleAnchor;
    }

    /**
     * Returns the defaultRenderer. If no renderer is registered for a specific class the <code>defaultRenderer</code>
     * will be used.
     *
     * @return the defaultRenderer
     */
    public GridCellRenderer getDefaultRenderer() {

        return cellRendererManager.getDefaultRenderer();
    }

    /**
     * Set the defaultRenderer. If no renderer is registered for a specific class the <code>defaultRenderer</code> will
     * be used.
     *
     * @param defaultRenderer the new defaultRenderer
     */
    public void setDefaultRenderer(final GridCellRenderer defaultRenderer) {

        cellRendererManager.setDefaultRenderer(defaultRenderer);
    }

    /**
     * Adds a renderer to the handler. The renderer is the default renderer for the cellClass <code>cls</code>
     *
     * @param cls      set the renderer for this class
     * @param renderer the renderer for all instances of <code>cls</code>
     */
    public void addRendererMapping(final Class<?> cls, final GridCellRenderer renderer) {

        cellRendererManager.addRendererMapping(cls, renderer);
    }
}
