package com.midorlo.batbelt.swing.cubeview.components;

import com.midorlo.batbelt.swing.cubeview.model.Block;
import com.midorlo.batbelt.swing.cubeview.model.IDisplay;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class EditorUI extends JPanel {

    private Map<String, IDisplay> displays = Map.of("Grid", new GridBlockUI(),
                                                    "List", new ListUI());
    private IDisplay              contentPane;
    private JComboBox<String>     uiSelection = createDisplaySelector();


    public EditorUI() {

        setLayout(new BorderLayout());
        add(uiSelection, BorderLayout.NORTH);

    }

    private JComboBox<String> createDisplaySelector() {

        JComboBox<String> stringJComboBox = new JComboBox<>(displays.keySet().toArray(new String[0]));
        stringJComboBox.addActionListener(e -> {
            log.info(e.getActionCommand());
            String selectedItem = (String) stringJComboBox.getSelectedItem();
            if (selectedItem.equals("Grid")) {
                remove((Component) contentPane);
                this.contentPane = new GridBlockUI();
                add((Component) contentPane, BorderLayout.CENTER);
            } else if (selectedItem.equals("List")) {
                remove((Component) contentPane);
                this.contentPane = new ListUI();
                add((Component) contentPane, BorderLayout.CENTER);
            }

            revalidate();
            repaint();


        });
        return stringJComboBox;
    }

    public void setBlock(Block block) {

        this.contentPane = new GridBlockUI();
        contentPane.setBlock(block);
        add((Component) contentPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
