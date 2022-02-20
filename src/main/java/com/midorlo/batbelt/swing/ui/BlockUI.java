package com.midorlo.batbelt.swing.ui;

import com.midorlo.batbelt.swing.model.Block;
import com.midorlo.batbelt.swing.model.Cube;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.WEST;

@Slf4j
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

        Integer columnCount = block.getTotalColumns();
        Integer rowCount    = block.getTotalRows();
        Integer layerCount  = block.getTotalLayers();

        CubeUI[][] cells = new CubeUI[columnCount][rowCount];
        log.info("Created Matrix with bounds {},{},{}", columnCount, rowCount, layerCount);

        for (int z = 0; z < layerCount; z++) {
            for (int x = 0; x < columnCount; x++) {
                for (int y = 0; y < rowCount; y++) {

                    Cube storedCube = block.getCubesMatrix()[x][y][z];
                    CubeUI uiCube = cells[x][y];

                    if (uiCube == null) {
                        if (canRender(storedCube)) {
                            uiCube          = new CubeUI(storedCube);
                            cells[x][y] = uiCube;
                            log.info("Created CubeUI [{}, {}, {}]  at [{},{},{}]",
                                     storedCube.getX(),
                                     storedCube.getY(),
                                     storedCube.getZ(),
                                     x,
                                     y,
                                     z
                            );
                        } else {
                            log.info("Not creating CubeUI at {},{},{}: Source object is null.", x, y, z);
                        }
                    } else {
                        log.info("Not creating CubeUI at {},{},{}: Slot is not empty.", x, y, z);
                    }


                    if (cells[x][y] == null && canRender(storedCube)) {
                        cells[x][y] = new CubeUI(storedCube);
                        log.info("Created Cell at ({},{}) (Layer {})", x, y, z);
                    } else {
                        log.info("Skipped creating Cell at ({},{}) (Layer {})", x, y, z);
                    }
                }
            }
        }
        log.info("Created all cells");
        return cells;
    }

    private void addCellsToBlockUI(CubeUI[][] cells) {

        Integer       columnCount = block.getTotalColumns();
        Integer       rowCount    = block.getTotalRows();
        GridBagLayout l           = new GridBagLayout();
        setLayout(l);

        log.info("Creating Document (columns: {}, rows: {})", columnCount, rowCount);

        for (int x = 0; x < columnCount; x++) {
            for (int y = 0; y < rowCount; y++) {

                CubeUI cell = cells[x][y];

                GridBagConstraints g = new GridBagConstraints();
                g.gridx      = x;
                g.gridy      = y;
                g.anchor     = WEST;
                g.gridwidth  = 1;
                g.gridheight = 1;
                g.insets.set(5, 5, 5, 5);
                add(cell, g);
                log.info("Added cell to document ({},{})", x, y);
            }
        }
        log.info("Created Document");
    }

    private boolean canRender(Cube cube) {

        return cube != null
               && cube.getTitle() != null
               && !cube.getTitle().isEmpty()
               && !cube.getTitle().isBlank();

    }
}
