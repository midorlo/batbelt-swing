package com.midorlo.batbelt.swing.ui;

import com.midorlo.batbelt.swing.model.Cube;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CubeUI extends JPanel {

    private final Cube cube;

    public CubeUI(Cube cube) {
        this.cube = cube;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(cube.getWidth(),cube.getHeight()));

        setBorder(new TitledBorder(getTitleString()));
    }

    private String getTitleString() {
        return String.format("X: %s Y: %s Z: %s",
                             cube.getX(),
                             cube.getY(),
                             cube.getZ());
    }
}
