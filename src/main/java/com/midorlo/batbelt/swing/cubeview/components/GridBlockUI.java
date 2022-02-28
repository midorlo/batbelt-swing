package com.midorlo.batbelt.swing.cubeview.components;

import com.midorlo.batbelt.swing.cubeview.components.CubeUI;
import com.midorlo.batbelt.swing.cubeview.components.GridBlockCellUI;
import com.midorlo.batbelt.swing.cubeview.model.Perspective;
import com.midorlo.batbelt.swing.cubeview.model.Block;
import com.midorlo.batbelt.swing.cubeview.model.IDisplay;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.WEST;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class GridBlockUI extends JScrollPane implements IDisplay {

    private final JPanel              canvas;
    private       GridBlockCellUI[][] cells;

    private Block       block;
    private Perspective perspective;

    public GridBlockUI(Block block) {

        this();
        setBlock(block);
    }

    public GridBlockUI() {

        JPanel canvas = new JPanel();
        canvas.setLayout(new GridBagLayout());
        canvas.setBorder(new TitledBorder("Canvas"));
        this.canvas = canvas;

        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        setBorder(new TitledBorder(getClass().getSimpleName()));
        setViewportView(canvas);
    }

    public void setBlock(Block block) {

        this.block = block;
        this.cells = new GridBlockCellUI[block.getTotalColumns()][block.getTotalRows()];
        createFrontalPerspective(block);
    }

    private void createFrontalPerspective(Block block) {

        block.getCubesList()
             .stream()
             .map(CubeUI::new)
             .forEach(this::addCell);
        revalidate();
        repaint();
    }

    private void addCell(CubeUI cubeUI) {

        GridBlockCellUI container = getOrInitCellContainer(cubeUI.getCube().getX(), cubeUI.getCube().getY());
        container.add(cubeUI);
        cubeUI.addActionListener(e -> container.getCardLayout().next(container));
        log.info("Placed CubeUI at ({},{}, {})",
                 cubeUI.getCube().getX(),
                 cubeUI.getCube().getY(),
                 cubeUI.getCube().getZ());
    }

    private GridBlockCellUI getOrInitCellContainer(int x, int y) {

        GridBlockCellUI container = cells[x][y];
        if (container == null) {
            container = new GridBlockCellUI();
            setBorder(new TitledBorder(String.format("X: %s Y: %s", x, y)));
            GridBagConstraints g = new GridBagConstraints();
            g.gridx      = x;
            g.gridy      = y;
            g.anchor     = WEST;
            g.gridwidth  = 1;
            g.gridheight = 1;
            g.insets.set(5, 5, 5, 5);
            cells[x][y] = container;
            canvas.add(container, g);


            log.info("Created Cell container at ({},{})", x, y);
        }
        return container;
    }
}
