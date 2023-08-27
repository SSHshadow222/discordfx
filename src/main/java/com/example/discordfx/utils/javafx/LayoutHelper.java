package com.example.discordfx.utils.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class LayoutHelper
{
    public static void addLabel(Label label, int index, Pane layout){
        Node node = layout.getChildren().get(index);
        boolean alreadyExists = isLabel(node) && (!(label.getId() == null) && Objects.equals(node.getId(), label.getId()));

        if (alreadyExists) {
            Label _label = (Label) node;
            _label.setText(label.getText());
            _label.setStyle(label.getStyle());
        }
        else {
            layout.getChildren().add(index, label);
        }
    }

    public static boolean isLabel(Node node){
        return node.getClass().equals(Label.class);
    }
}
