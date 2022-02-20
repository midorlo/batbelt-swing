package com.midorlo.batbelt.swing.ui;

import com.midorlo.batbelt.swing.model.Cube;
import com.midorlo.batbelt.swing.ui.property.ComponentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class CubeUI extends JToggleButton {

    private Cube           cube;
    private ComponentState componentState = new ComponentState();

    public CubeUI(Cube cube) {

        this();
        setCube(cube);
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
