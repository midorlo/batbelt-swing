package com.midorlo.batbelt.swing.util;

import com.midorlo.batbelt.swing.model.Block;
import com.midorlo.batbelt.swing.model.Cube;

import java.util.List;
import java.util.stream.*;

public class ModelFactory {

    public static Block createBlock(int toX,
                                    int toY,
                                    int toZ) {

        return createBlock(0, toX, 0, toY, 0, toZ);
    }

    public static Block createBlock(int fromX,
                                    int toX,
                                    int fromY,
                                    int toY,
                                    int fromZ,
                                    int toZ) {

        return new Block(createCubes(fromX, toX, fromY, toY, fromZ, toZ));
    }

    public static List<Cube> createCubes(int fromX,
                                         int toX,
                                         int fromY,
                                         int toY,
                                         int fromZ,
                                         int toZ) {

        return IntStream.range(fromX, toX)
                        .mapToObj(x -> IntStream.range(fromY, toY)
                                                .mapToObj(y -> IntStream.range(fromZ, toZ)
                                                                        .mapToObj(z -> new Cube(x, y, z)))
                                                .flatMap(Stream::parallel))
                        .flatMap(Stream::parallel)
                        .collect(Collectors.toList());
    }

    public static List<Cube> createCubes(int toX, int toY, int toZ) {return createCubes(0, toX, 0, toY, 0, toZ);}
}
