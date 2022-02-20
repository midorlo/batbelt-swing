package com.midorlo.batbelt.swing.model;

import lombok.Data;

@Data
public class Cube {

    private Integer x;
    private Integer y;
    private Integer z;
    private Integer w = 100;
    private Integer h = 150;
    private Integer d = 100;

    private String title = "Cube";

    public Cube(Integer x, Integer y, Integer z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }
}
