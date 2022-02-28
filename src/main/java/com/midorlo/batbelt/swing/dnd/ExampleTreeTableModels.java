package com.midorlo.batbelt.swing.dnd;

/**
 * Methods to create simple {@link TreeTableModel} instances
 */
public class ExampleTreeTableModels {

    /**
     * Create a simple dummy {@link TreeTableModel}
     *
     * @return The {@link TreeTableModel}
     */
    public static TreeTableModel createSimple() {

        Object root = "root";
        TreeTableModel treeTableModel = new AbstractTreeTableModel(root) {
            @Override
            public Object getChild(Object node, int childIndex) {

                if (node.toString().startsWith("root")) {
                    return "child" + childIndex;
                }
                if (node.toString().startsWith("child")) {
                    return "leaf" + childIndex;
                }
                return null;
            }

            @Override
            public int getChildCount(Object node) {

                if (node.toString().startsWith("root")) {
                    return 3;
                }
                if (node.toString().startsWith("child")) {
                    return 2;
                }
                return 0;
            }

            @Override
            public int getColumnCount() {

                return 3;
            }

            @Override
            public String getColumnName(int column) {

                return "column" + column;
            }

            @Override
            public Class<?> getColumnClass(int column) {

                if (column == 0) {
                    return TreeTableModel.class;
                }
                return Object.class;
            }

            @Override
            public Object getValueAt(Object node, int column) {

                return node + ", column " + column;
            }
        };
        return treeTableModel;
    }
}
