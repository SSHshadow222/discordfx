package com.example.discordfx.utils.javafx.node;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class MessagePane extends HBox
{
    @FXML
    public ImageView imageView;
    @FXML
    public Label labelUsername;
    @FXML
    public Pane layoutMessages;
    private long userId;

    public MessagePane(){
        loadFxmlFile();
    }

    public MessagePane(Image image, String username, long userId){
        loadFxmlFile();
        setImage(image);
        setUsername(username);
        setUserId(userId);
    }

    // region Getters and Setters

    public Image getImageView() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }

    public void setUsername(String text) {
        this.labelUsername.setText(text);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    // endregion

    public void addMessage(String txt){
        if (txt == null || txt.equals("")){
            throw new RuntimeException("Null or empty messages cannot be appended to the layout!\n");
        }

        Text text = new Text(txt);
        text.setFill(Color.LIGHTGRAY);
        text.setStyle("-fx-font-size: 15px");

        TextFlow textFlow = new TextFlow(text);
        layoutMessages.getChildren().add(textFlow);
    }

    private void loadFxmlFile() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/discordfx/fxml/node/MessagePane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
