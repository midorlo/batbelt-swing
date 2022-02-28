package com.midorlo.batbelt.swing.cubeview.model;


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

    public Integer getX() {

        return x;
    }

    public void setX(Integer x) {

        this.x = x;
    }
    public Integer getY() {

        return y;
    }

    public void setY(Integer y) {

        this.y = y;
    }
    public Integer getZ() {

        return z;
    }

    public void setZ(Integer z) {

        this.z = z;
    }

    public Integer getW() {

        return w;
    }

    public void setW(Integer w) {

        this.w = w;
    }

    public Integer getH() {

        return h;
    }

    public void setH(Integer h) {

        this.h = h;
    }

    public Integer getD() {

        return d;
    }

    public void setD(Integer d) {

        this.d = d;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }
}
