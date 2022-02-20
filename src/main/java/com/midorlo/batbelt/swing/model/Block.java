package com.midorlo.batbelt.swing.model;

import lombok.Data;

import java.util.List;


@Data
public class Block {

    private final List<Cube> cubesList;
    private final Cube[][][] cubesMatrix;
    private final BlockMeta  blockMeta;

    public Block(List<Cube> cubes) {

        this.cubesList   = cubes;
        this.blockMeta   = new BlockMeta(cubes);
        this.cubesMatrix = generateCubeMatrix(cubes);
    }


    private Cube[][][] generateCubeMatrix(List<Cube> cubes) {

        Integer    columnCount = getBlockMeta().getColumCount();
        Integer    rowCount    = getBlockMeta().getRowCount();
        Integer    layerCount  = getBlockMeta().getLayerCount();
        Cube[][][] cubesMatrix = new Cube[blockMeta.maxX + 1][blockMeta.maxY + 1][blockMeta.maxZ + 1];
        System.out.printf("Created Cubes Matrix with bounds (%s,%s,%s)\n", columnCount, rowCount, layerCount);

        System.out.printf("Inserting %s cubes\n", cubes.size());
        for (Cube cube : cubes) {
            int column = cube.getX();
            int row    = cube.getY();
            int layer  = cube.getZ();
            cubesMatrix[column][row][layer] = cube;
            System.out.printf("Inserted Cube (%s,%s,%s)\n", column, row, layer);
        }
        System.out.println("Inserted all Cubes");
        return cubesMatrix;
    }

    @Data
    public static class BlockMeta {

        private final Integer width;
        private final Integer height;
        private final Integer depth;

        private final Integer maxX;
        private final Integer maxY;
        private final Integer maxZ;

        private final Integer minX;
        private final Integer minY;
        private final Integer minZ;

        public BlockMeta(List<Cube> cubes) {

            final int[] minX = {Integer.MAX_VALUE};
            final int[] minY = {Integer.MAX_VALUE};
            final int[] minZ = {Integer.MAX_VALUE};
            final int[] maxY = {Integer.MIN_VALUE};
            final int[] maxX = {Integer.MIN_VALUE};
            final int[] maxZ = {Integer.MIN_VALUE};

            cubes.forEach(cube -> {//@formatter:off
                minX[0] = (cube.getX() < minX[0]) ? cube.getX() : minX[0];
                maxX[0] = (cube.getX() > maxX[0]) ? cube.getX() : maxX[0];
                minY[0] = (cube.getY() < minY[0]) ? cube.getY() : minY[0];
                maxY[0] = (cube.getY() > maxY[0]) ? cube.getY() : maxY[0];
                minZ[0] = (cube.getZ() < minZ[0]) ? cube.getZ() : minZ[0];
                maxZ[0] = (cube.getZ() > maxZ[0]) ? cube.getZ() : maxZ[0];
            });

            this.minX = minX[0];
            this.minY = minY[0];
            this.minZ = minZ[0];
            this.maxX = maxX[0];
            this.maxY = maxY[0];
            this.maxZ = maxZ[0];

            if (cubes.size() > 0) {
                width  = (maxX[0] * cubes.get(0).getWidth());
                height = (maxX[0] * cubes.get(0).getHeight());
                depth  = (maxX[0] * cubes.get(0).getDepth());
            } else {
                width  = 0;
                height = 0;
                depth  = 0;
            }//@formatter:on
        }

        public Integer getColumCount() {

            return maxX + 1;
        }

        public Integer getRowCount() {

            return maxY + 1;
        }

        public Integer getLayerCount() {

            return maxZ + 1;
        }
    }
}
