package com.midorlo.batbelt.swing.cubeview.model;

import lombok.Data;

@Data
public class ComponentState {

    private boolean isEnabled;
    private boolean isVisible;
    private boolean isSelected;
    private boolean isEditable;
}
