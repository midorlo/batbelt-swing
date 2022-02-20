package com.midorlo.batbelt.swing;

import com.midorlo.batbelt.swing.ui.AppUI;
import com.midorlo.batbelt.swing.ui.BlockUI;
import com.midorlo.batbelt.swing.util.ModelFactory;

import javax.swing.*;

public class AppBatbeltSwing {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AppUI ui = new AppUI();
            ui.setContentPane(new BlockUI(ModelFactory.createBlock(3, 3, 3)));
        });
    }
}
