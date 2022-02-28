package com.midorlo.batbelt.swing.dnd;

import javax.swing.*;

/**
 * A simple example application for the JTreeTable
 */
@SuppressWarnings("javadoc")
public class JTreeTableSimpleExample {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TreeTableModel treeTableModel = ExampleTreeTableModels.createSimple();
        JTreeTable     treeTable      = new JTreeTable(treeTableModel);
        f.getContentPane().add(new JScrollPane(treeTable));

        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
