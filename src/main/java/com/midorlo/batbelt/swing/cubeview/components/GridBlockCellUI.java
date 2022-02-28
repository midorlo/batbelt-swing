package com.midorlo.batbelt.swing.cubeview.components;

import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Data
public class GridBlockCellUI extends JPanel {

    CardLayout cardLayout;

    public GridBlockCellUI() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }



}
