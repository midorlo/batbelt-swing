package com.midorlo.batbelt.swing;

import com.formdev.flatlaf.FlatLightLaf;
import com.midorlo.batbelt.swing.cubeview.CubeView;
import com.midorlo.batbelt.swing.cubeview.components.EditorUI;
import com.midorlo.batbelt.swing.cubeview.model.Block;
import com.midorlo.batbelt.swing.cubeview.util.ModelFactory;

import javax.swing.*;

public class KSwingset extends JFrame{

    static {
        FlatLightLaf.setup();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EditorUI editorUI = new EditorUI();
            Block    block    = ModelFactory.createBlock(11, 22, 3);
            editorUI.setBlock(block);

            CubeView ui = new CubeView();
            ui.setContentPane(editorUI);
        });
    }
}
