package com.midorlo.batbelt.swing.cubeview.components;

import com.midorlo.batbelt.swing.cubeview.model.Cube;
import com.midorlo.batbelt.swing.cubeview.model.ComponentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

@Data
@EqualsAndHashCode(callSuper = true)
public class CubeUI extends JButton {

    private static Random r = new Random();

    private Cube           cube;
    private ComponentState componentState = new ComponentState();

    public CubeUI(Cube cube) {

        this();
        setCube(cube);
        setBackground(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
    }

    public CubeUI() {

        setPreferredSize(new Dimension(100, 150));
        setLayout(new BorderLayout());
    }

    public void setCube(@NotNull Cube cube) {

        this.cube = cube;
        setPreferredSize(new Dimension(cube.getW(), cube.getH()));
        setBorder(new TitledBorder(getTitle()));
    }

    private String getTitle() {

        return (cube != null)
               ? String.format("X: %s Y: %s Z: %s", cube.getX(), cube.getY(), cube.getZ())
               : "Empty";
    }
}
