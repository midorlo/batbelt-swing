package com.midorlo.batbelt.swing.model;

import com.midorlo.batbelt.swing.model.property.BlockSide;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Data
@Slf4j
public class Block {

    private Cube[][][] cubesMatrix  = new Cube[0][0][0];
    private Integer    minX         = Integer.MAX_VALUE;
    private Integer    maxX         = Integer.MIN_VALUE;
    private Integer    minY         = Integer.MAX_VALUE;
    private Integer    maxY         = Integer.MIN_VALUE;
    private Integer    minZ         = Integer.MAX_VALUE;
    private Integer    maxZ         = Integer.MIN_VALUE;
    private Integer    totalColumns = 0;
    private Integer    totalRows    = 0;
    private Integer    totalLayers  = 0;
    private Integer    width        = 0;
    private Integer    height       = 0;
    private Integer    depth        = 0;

    public Block(List<Cube> cubes) {

        setCubes(cubes);
    }

    /**
     * @implNote O(2N)
     */
    public void setCubes(List<Cube> cubes) {

        if (cubes != null && cubes.size() > 0) {
            cubes.forEach(this::introspect);
            cubesMatrix = new Cube[totalColumns][totalRows][totalLayers];
            cubes.forEach(this::addCube);
        }
    }

    private void introspect(Cube cube) {
        //@formatter:off
        minX         = (cube.getX() < minX) ? cube.getX() : minX;
        maxX         = (cube.getX() > maxX) ? cube.getX() : maxX;
        minY         = (cube.getY() < minY) ? cube.getY() : minY;
        maxY         = (cube.getY() > maxY) ? cube.getY() : maxY;
        minZ         = (cube.getZ() < minZ) ? cube.getZ() : minZ;
        maxZ         = (cube.getZ() > maxZ) ? cube.getZ() : maxZ;
        width        = (maxX * cube.getW());
        height       = (maxX * cube.getH());
        depth        = (maxX * cube.getD());
        totalColumns = maxX + 1;
        totalRows    = maxY + 1;
        totalLayers  = maxZ + 1;
        //@formatter:on
    }

    public void addCube(Cube cube) {

        cubesMatrix[cube.getX()][cube.getY()][cube.getZ()] = cube;
    }

    public Boolean existsCubeAt(Integer x, Integer y, Integer z) {

        return cubesMatrix[x][y][z] != null;
    }

    public Cube getCubeAt(Integer x, Integer y, Integer z) {

        return cubesMatrix[x][y][z];
    }

}
