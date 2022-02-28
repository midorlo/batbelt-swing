package com.midorlo.batbelt.swing.cubeview.components;

import com.midorlo.batbelt.swing.cubeview.model.Block;
import com.midorlo.batbelt.swing.cubeview.model.Cube;
import com.midorlo.batbelt.swing.cubeview.model.Perspective;
import com.midorlo.batbelt.swing.cubeview.model.IDisplay;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ListUI extends JList<String> implements IDisplay {

    @Override
    public void setBlock(Block block) {
        setBorder(new TitledBorder("List"));
        DefaultListModel<String> model = new DefaultListModel<>();
        block.getCubesList().stream().map(Cube::getTitle).forEach(model::addElement);
        setModel(model);
        revalidate();
        repaint();


    }

    @Override
    public void setPerspective(Perspective perspective) {

    }
}
