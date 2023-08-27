package com.example.discordfx.controller;

import com.example.discordfx.service.Service;
import com.example.discordfx.service.UserService;
import com.example.discordfx.service.dto.UserDto;
import com.example.discordfx.utils.message.Color;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login extends Controller {

    @FXML
    public TextField textFieldLogin;
    @FXML
    public PasswordField textFieldPasswd;
    @FXML
    public Label labelLoginInfo1, labelLoginInfo2, labelPasswordInfo1, labelPasswordInfo2,
            labelForgotPasswd, labelRegister;
    @FXML
    public Button buttonLogin;
    private UserService service;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeListeners();
    }

    // region Getters and Setters
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);

        Stage currentStage = this.stage;
        currentStage.setTitle("Login");
        currentStage.setResizable(false);
        currentStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/example/discordfx/image/Discord512x512.png")).toString()));

        this.stage = currentStage;
    }

    public Service[] getServices() {
        return new Service[]{this.service};
    }

    public void setServices(Service... service) {
        try {
            this.service = (UserService) service[0];
        } catch (Exception ignored) {
            this.service = new UserService();
        }

        load();
    }
    // endregion

    // region Event Handlers
    @FXML
    public void handleRegistering() throws IOException {
        URL url = getClass().getResource("/com/example/discordfx/fxml/stage/Register.fxml");
        Stage currentStage = getStage();
        changeStage(currentStage, url);
    }

    @FXML
    public void handleLogging() throws IOException {
        String email, passwd;
        email = textFieldLogin.getText();
        passwd = textFieldPasswd.getText();

        if (Objects.equals(email, "") || Objects.equals(passwd, "")) {
            adviseUser(true);
        } else {
            UserDto user = service.authenticate(email, passwd);
            if (user == null) adviseUser(false);
            else {
                URL url = getClass().getResource("/com/example/discordfx/fxml/stage/Logged.fxml");
                Stage currentStage = getStage();
                Logged.loggedUser = user;
                changeStage(currentStage, url);
            }
        }
    }

    @FXML
    public void handleTextFieldPasswdKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogging();
        }
    }

    @FXML
    public void handleTextFieldLoginKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogging();
        }
    }

    @FXML
    public void handleButtonLoginKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogging();
        }
    }
    // endregion

    private void initializeListeners() {
        textFieldLogin.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) (newValue) > 40) {
                String text = textFieldLogin.getText();
                textFieldLogin.setText(text.substring(0, text.length() - 1));
            }
        });
        textFieldLogin.onKeyPressedProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    private void load(){
        changeDisplay(2);
    }

    /**
     * Informs the user about the wrongness of the given information
     */
    private void adviseUser(boolean emptyFields) {
        if (emptyFields) {
            String login = textFieldLogin.getText(),
                    passwd = textFieldLogin.getText(),
                    defaultMessage = "*",
                    warnMessage = "- This field is required";

            Font defaultFont = Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15),
                    warningFont = Font.font("Calibri", FontWeight.NORMAL, FontPosture.ITALIC, 15);


            if (Objects.equals(login, "")) {
                labelLoginInfo1.setTextFill(Color.RED);
                labelLoginInfo2.setFont(warningFont);
                labelLoginInfo2.setText(warnMessage);
            } else {
                labelLoginInfo1.setTextFill(Color.WHITE);
                labelLoginInfo2.setFont(defaultFont);
                labelLoginInfo2.setText(defaultMessage);
            }
            if (Objects.equals(passwd, "")) {
                labelPasswordInfo1.setTextFill(Color.RED);
                labelPasswordInfo2.setFont(warningFont);
                labelPasswordInfo2.setText(warnMessage);
            } else {
                labelPasswordInfo1.setTextFill(Color.WHITE);
                labelPasswordInfo2.setFont(defaultFont);
                labelPasswordInfo2.setText(defaultMessage);
            }
        } else {
            labelLoginInfo1.setTextFill(Color.RED);
            labelPasswordInfo1.setTextFill(Color.RED);

            String message = "- Login or password is invalid";
            labelLoginInfo2.setText(message);
            labelPasswordInfo2.setText(message);

            Font font = Font.font("Calibri", FontWeight.NORMAL, FontPosture.ITALIC, 15);
            labelLoginInfo2.setFont(font);
            labelPasswordInfo2.setFont(font);
        }
    }
}
