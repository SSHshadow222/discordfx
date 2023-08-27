package com.example.discordfx.controller;

import com.example.discordfx.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    public static boolean SHOWED_DISPLAY_WARNING = false;
    protected Stage stage;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // region Getters and Setters
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        stage.setTitle("Title not set");
        this.stage = stage;
    }

    public abstract Service[] getServices();

    public abstract void setServices(Service... service);
    // endregion

    /**
     * Closes the current stage/window and opens another based on the given url
     *
     * @param currentStage The currently active scene
     * @param url          The url for the fxml file representing the next stage
     * @throws IOException
     */
    protected void changeStage(Stage currentStage, URL url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        Pane layout = fxmlLoader.load();
        double minWidth = layout.getMinWidth(),
                minHeight = layout.getMinHeight();

        Stage nextStage = new Stage();
        nextStage.setScene(new Scene(layout, minWidth, minHeight));
        nextStage.setMinWidth(minWidth);
        nextStage.setMinHeight(minHeight);

        Controller controller = fxmlLoader.getController();
        controller.setStage(nextStage);
        controller.setServices(getServices());

        nextStage.show();
        currentStage.close();
    }

    /**
     * Moves the stage/window to a given display. If the display is not found within the system, a warning message will be shown in the console and the application will continue to run on the primary display.
     * @param displayNo The number of the display ranging from 1 to n, where n is the number of displays available within the system.
     */
    protected void changeDisplay(int displayNo){
        try {
            Screen display = Screen.getScreens().get(displayNo-1);
            Rectangle2D bounds = display.getVisualBounds();

            getStage().setX(bounds.getMinX());
            getStage().setY(bounds.getMinY());
            getStage().centerOnScreen();
        }
        catch (IndexOutOfBoundsException e){
            if (!Controller.SHOWED_DISPLAY_WARNING) {
                System.out.println("Warning: Display " + displayNo + " cannot be found on the system! The application will continue to run on the primary display.");
                Controller.SHOWED_DISPLAY_WARNING = true;
            }
        }
    }
}
