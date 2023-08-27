package com.example.discordfx.utils.javafx.node;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class MyMenuItem extends MenuItem {
    private MyContextMenu parent;

    // region Constructors
    public MyMenuItem(String text, MyContextMenu parent) {
        super(text.toUpperCase());
        this.parent = parent;
    }

    public MyMenuItem(String text, Node graphic, MyContextMenu parent) {
        super(text.toUpperCase(), graphic);
        this.parent = parent;
    }
    // endregion

    // region Getters and Setters
    public MyContextMenu getParent() {
        return parent;
    }

    public void setParent(MyContextMenu parent) {
        this.parent = parent;
    }
    // endregion
}
