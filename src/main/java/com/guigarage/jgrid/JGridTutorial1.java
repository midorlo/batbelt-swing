package com.guigarage.jgrid;

import com.guigarage.jgrid.ui.BasicGridUI;
import com.guigarage.jgrid.ui.MacOsGridUI;

import javax.swing.*;


public class JGridTutorial1 extends JFrame {

    private static final long serialVersionUID = 1L;

    public JGridTutorial1() {

        setTitle("Simple JGrid Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JGrid grid = new JGrid();

        getContentPane().add(new JScrollPane(grid));

        DefaultListModel<Integer> model = new DefaultListModel<>();
        for (int i = 0; i < 100; i++) {
            model.addElement(i);
        }
        grid.setModel(model);
        grid.setFont(grid.getFont().deriveFont(40.0f));
        setSize(1024, 768);
        setLocationRelativeTo(null);
        grid.setUI(new BasicGridUI());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new JGridTutorial1().setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
