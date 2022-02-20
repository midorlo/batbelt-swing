package com.midorlo.batbelt.swing.model;

import lombok.Data;

@Data
public class Cube {

    private Integer x;
    private Integer y;
    private Integer z;

    private Integer width  = 100;
    private Integer height = 150;
    private Integer depth  = 100;

    private Boolean filtered = false;

    public Cube(Integer x, Integer y, Integer z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }
}
