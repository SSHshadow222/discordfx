package com.example.discordfx.controller;

import com.example.discordfx.exception.RepoException;
import com.example.discordfx.exception.ValidException;
import com.example.discordfx.service.Service;
import com.example.discordfx.service.UserService;
import com.example.discordfx.service.dto.UserDto;
import com.example.discordfx.utils.message.Message;
import com.example.discordfx.utils.message.MessageState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.Objects;
import java.util.ResourceBundle;

public class Register extends Controller {
    @FXML
    public ComboBox<Integer> comboBoxAge;
    @FXML
    public TextField textFieldFirst,
            textFieldLast,
            textFieldEmail;
    @FXML
    public PasswordField textFieldPasswd;
    @FXML
    public Label labelTermsOfService,
            labelAlreadyHaveAccount,
            labelWarnings;

    @FXML
    public Button buttonContinue;
    @FXML
    public CheckBox checkBoxTermsOfService;
    private UserService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelWarnings.setVisible(false);
        buttonContinue.setDisable(true);
        comboBoxAge.setValue(Year.now().getValue() - 6);
        ObservableList<Integer> items = FXCollections.observableArrayList();
        for (int i = Year.now().getValue() - 3; i >= Year.now().getValue() - 152; i--) {
            items.add(i);
        }
        comboBoxAge.setItems(items);

        initializeListeners();
    }

    // region Getters and Setters
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);

        Stage currentStage = this.stage;
        currentStage.setTitle("Register");
        currentStage.setResizable(false);
        currentStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/example/discordfx/image/Discord512x512.png")).toString()));

        this.stage = currentStage;
    }

    public Service[] getServices() {
        return new UserService[]{this.service};
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
    public void handleContinue() throws IOException {
        String email = textFieldEmail.getText(),
                first = textFieldFirst.getText(),
                last = textFieldLast.getText(),
                passwd = textFieldPasswd.getText();

        int year = comboBoxAge.getValue();

        try {
            boolean alreadyExists = service.get(email) != null;
            if (alreadyExists) {
                throw new RepoException("Email has already been taken!\n");
            }
            UserDto user = service.add(first, last, passwd, Year.now().getValue() - year, email);

            URL url = getClass().getResource("/com/example/discordfx/fxml/stage/Logged.fxml");
            Stage currentStage = getStage();
            changeStage(currentStage, url, user);
        } catch (ValidException ve) {
            updateWarningMessage(MessageState.WARNING, ve.getMessage());
        } catch (RepoException re) {
            updateWarningMessage(MessageState.ERROR, re.getMessage());
        }
    }

    @FXML
    public void handleLogin() throws IOException {
        URL url = getClass().getResource("/com/example/discordfx/fxml/stage/Login.fxml");
        Stage currentStage = getStage();
        changeStage(currentStage, url);
    }

    @FXML
    public void handleTermsOfService() throws IOException {
        System.out.println("Terms of service");
    }
    // endregion

    private void initializeListeners() {
        textFieldEmail.textProperty().addListener((observable, oldValue, newValue) -> updateButtonContinueDisabledState());
        textFieldFirst.textProperty().addListener((observable, oldValue, newValue) -> updateButtonContinueDisabledState());
        textFieldLast.textProperty().addListener((observable, oldValue, newValue) -> updateButtonContinueDisabledState());
        textFieldPasswd.textProperty().addListener((observable, oldValue, newValue) -> updateButtonContinueDisabledState());
        checkBoxTermsOfService.selectedProperty().addListener((observable, oldValue, newValue) -> updateButtonContinueDisabledState());
    }

    private void load(){
        changeDisplay(2);
    }

    /**
     * Enables/disables the continue button based on the information given by the user
     */
    private void updateButtonContinueDisabledState() {
        boolean canContinue = !textFieldEmail.getText().equals("") && !textFieldFirst.getText().equals("") &&
                !textFieldLast.getText().equals("") && !textFieldPasswd.getText().equals("") &&
                checkBoxTermsOfService.isSelected();

        buttonContinue.setDisable(!canContinue);
    }

    protected void changeStage(Stage currentStage, URL url, UserDto user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        Pane layout = fxmlLoader.load();
        double minWidth = layout.getMinWidth(),
                minHeight = layout.getMinHeight();

        Stage nextStage = new Stage();
        nextStage.setScene(new Scene(layout, minWidth, minHeight));
        nextStage.setMinWidth(minWidth);
        nextStage.setMinHeight(minHeight);

        Logged controller = fxmlLoader.getController();
        controller.setStage(nextStage);
        controller.setLoggedUser(user);
        controller.setServices(getServices());

        nextStage.show();
        currentStage.close();
    }

    /**
     * Informs the user about the mistakes made
     *
     * @param state   The state of the message (warning/error)
     * @param message The message to be displayed
     */
    private void updateWarningMessage(MessageState state, String message) {
        labelWarnings.setTextFill(Message.getColorByState(state));
        if (state == MessageState.WARNING)
            labelWarnings.setText("Warning: ");
        else
            labelWarnings.setText("Error: ");

        labelWarnings.setText(labelWarnings.getText() + message);
        labelWarnings.setUnderline(!labelWarnings.isUnderline());
        labelWarnings.setVisible(true);
    }
}
