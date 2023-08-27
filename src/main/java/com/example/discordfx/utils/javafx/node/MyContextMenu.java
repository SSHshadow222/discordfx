package com.example.discordfx.utils.javafx.node;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MyContextMenu extends ContextMenu {
    private Node parent;

    // region Constructors
    public MyContextMenu() {
        super();
    }

    public MyContextMenu(MenuItem... items) {
        super(items);
    }
    // endregion

    // region Getters and Setters

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    //endregion
}
