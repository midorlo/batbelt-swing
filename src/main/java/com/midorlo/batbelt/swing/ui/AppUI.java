package com.midorlo.batbelt.swing.ui;

import com.formdev.flatlaf.FlatLightLaf;
// This is importing the `Block` class from the `model` package.

import javax.swing.*;
import java.awt.*;

public class AppUI extends JFrame {

    static {
        FlatLightLaf.setup();
    }

    public AppUI() {

        super("Test");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
