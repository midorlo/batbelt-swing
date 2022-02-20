package com.midorlo.batbelt.swing.ui;

import com.midorlo.batbelt.swing.model.Block;
import com.midorlo.batbelt.swing.model.Cube;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.WEST;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlockUI extends JPanel {

    private final Block      block;
    private final CubeUI[][] cells;

    public BlockUI(Block block) {

        this.block = block;
        this.cells = createCells(block);
        addCellsToBlockUI(cells);
    }

    private CubeUI[][] createCells(Block block) {

        Integer columnCount = block.getBlockMeta()
                                   .getColumCount();
        Integer rowCount = block.getBlockMeta()
                                .getRowCount();
        Integer layerCount = block.getBlockMeta()
                                  .getLayerCount();

        CubeUI[][] cells = new CubeUI[columnCount][rowCount];
        System.out.printf("Created Matrix with bounds (%s,%s,%s)\n", columnCount, rowCount, layerCount);

        for (int z = 0; z < layerCount; z++) {
            for (int x = 0; x < columnCount; x++) {
                for (int y = 0; y < rowCount; y++) {

                    Cube   cube = block.getCubesMatrix()[x][y][z];
                    CubeUI cell = cells[x][y];

                    if (cube != null && cell == null && !cube.getFiltered()) {
                        cells[x][y] = new CubeUI(cube);
                        System.out.printf("Created Cell at (%s,%s) (Layer %s)\n", x, y, z);
                    } else {
                        System.out.printf("Skipped creating Cell at (%s,%s) (Layer %s)\n", x, y, z);
                    }
                }
            }
        }
        System.out.println("Created all cells");
        return cells;
    }

    private void addCellsToBlockUI(CubeUI[][] cells) {

        Integer columnCount = block.getBlockMeta()
                                   .getColumCount();
        Integer rowCount = block.getBlockMeta()
                                .getRowCount();
        GridBagLayout l = new GridBagLayout();
        setLayout(l);

        System.out.printf("Creating Document (columns: %s, rows: %s)\n", columnCount, rowCount);

        for (int x = 0; x < columnCount; x++) {
            for (int y = 0; y < rowCount; y++) {

                CubeUI cell = cells[x][y];

                GridBagConstraints g = new GridBagConstraints();
                g.gridx      = x;
                g.gridy      = y;
                g.anchor=WEST;
                g.gridwidth  = 1;
                g.gridheight = 1;
                g.insets.set(5, 5, 5, 5);
                add(cell, g);
                System.out.printf("Added cell to document (%s,%s)\n", x, y);
            }
        }
        System.out.println("Created Document");
    }
}
