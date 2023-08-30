package com.example.discordfx;

import com.example.discordfx.controller.Controller;
import com.example.discordfx.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = getClass().getResource("fxml/stage/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        
        Pane layout = fxmlLoader.load();
        double minWidth = layout.getMinWidth(),
                minHeight = layout.getMinHeight();

        stage.setScene(new Scene(layout, minWidth, minHeight));

        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setServices(new UserService());

        stage.show();
    }
}