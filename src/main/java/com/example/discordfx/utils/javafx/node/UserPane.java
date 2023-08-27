package com.example.discordfx.utils.javafx.node;

import com.example.discordfx.service.dto.UserDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class UserPane extends VBox {
    @FXML
    public ImageView imageView;
    @FXML
    public Label labelUsername, labelId, labelAge, labelEmail, labelPhone;
    private UserDto user;

    public UserPane() {
        user = getEmptyUser();
        initialize();
        hideDetails();
    }

    public UserPane(UserDto user, boolean showDetails) {
        this.user = user;
        initialize();
        if(!showDetails){
            hideDetails();
        }
    }

    private void initialize() {
        loadFxmlFile();
        resetLabels();
    }

    // region Getters and Setters

    public UserDto getUser(){
        return this.user;
    }

    public void setUser(UserDto user){
        this.user = user;
        resetLabels();
    }

    // endregion

    public void hideDetails(){
        VBox vBox = (VBox) labelUsername.getParent().getParent();
        while (vBox.getChildren().size() > 1){
            vBox.getChildren().remove(1);
        }
        vBox.getChildren().add(labelId);
    }

    public void showDetails(){
        VBox vBox = (VBox) labelUsername.getParent().getParent();
        if (vBox.getChildren().size() == 2){
            vBox.getChildren().add(labelAge);
            vBox.getChildren().add(labelEmail);
            vBox.getChildren().add(labelPhone);

            HBox hBox = (HBox) vBox.getChildren().get(0);
            hBox.getChildren().add(labelId);

            resetLabels();
        }
    }

    private void loadFxmlFile() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/discordfx/fxml/node/UserPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Image getImageFromUrl(URL url) {
        return new Image(url.toString());
    }

    private static UserDto getEmptyUser() {
        return new UserDto(1,"user", "name", 13, null, null, "Discord48x48.png");
    }

    private void resetLabels(){
        imageView.setImage(getImageFromUrl(Objects.requireNonNull(getClass().getResource("/com/example/discordfx/image/" + user.getPictureId()))));
        labelUsername.setText(user.getFirst() + " " + user.getLast());
        labelAge.setText("Age: " + user.getAge());
        labelEmail.setText("Email: " + user.getEmail());
        labelPhone.setText("Phone No: " + user.getPhone());
        labelId.setText(String.format("#%04d", user.getId()));
    }
}
