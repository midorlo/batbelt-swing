package com.midorlo.batbelt.swing.dnd;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

/**
 * This is a wrapper class takes a TreeTableModel and implements the table model
 * interface. The implementation is trivial, with all of the event dispatching
 * support provided by the superclass: the AbstractTableModel.
 */
class TreeTableModelAdapter extends AbstractTableModel {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = 8489726674002235447L;

    /**
     * The tree that backs this adapter. When the tree is expanded or
     * collapsed, the table model is updated accordingly
     */
    private final JTree tree;

    /**
     * The backing {@link TreeTableModel}
     */
    private final TreeTableModel treeTableModel;

    /**
     * Default constructor
     *
     * @param treeTableModel The backing {@link TreeTableModel}
     * @param tree           The tree
     */
    TreeTableModelAdapter(com.midorlo.batbelt.swing.dnd.TreeTableModel treeTableModel, JTree tree) {

        this.tree           = tree;
        this.treeTableModel = treeTableModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            // Don't use fireTableRowsInserted() here; the selection model
            // would get updated twice.
            @Override
            public void treeExpanded(TreeExpansionEvent event) {

                fireTableDataChanged();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {

                fireTableDataChanged();
            }
        });

        // Install a TreeModelListener that can update the table when
        // tree changes. We use delayedFireTableDataChanged as we can
        // not be guaranteed the tree will have finished processing
        // the event before us.
        treeTableModel.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {

                delayedFireTableDataChanged();
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {

                delayedFireTableDataChanged();
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {

                delayedFireTableDataChanged();
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {

                delayedFireTableDataChanged();
            }
        });
    }

    /**
     * Invokes fireTableDataChanged after all the pending events have been
     * processed. SwingUtilities.invokeLater is used to handle this.
     */
    private void delayedFireTableDataChanged() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                fireTableDataChanged();
            }
        });
    }

    @Override
    public String getColumnName(int column) {

        return treeTableModel.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int column) {

        return treeTableModel.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {

        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    @Override
    public int getRowCount() {

        return tree.getRowCount();
    }

    @Override
    public int getColumnCount() {

        return treeTableModel.getColumnCount();
    }

    @Override
    public Object getValueAt(int row, int column) {

        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    /**
     * Returns the tree node that is located in the specified row
     *
     * @param row The row
     *
     * @return The tree node
     */
    private Object nodeForRow(int row) {

        TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }
}
