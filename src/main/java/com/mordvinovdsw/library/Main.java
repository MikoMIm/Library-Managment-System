package com.mordvinovdsw.library;

import com.mordvinovdsw.library.utils.SceneController;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import com.mordvinovdsw.library.utils.StageUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static SceneController sceneController;

    @Override
    public void start(Stage stage) throws IOException {
        sceneController = new SceneController(stage);
        sceneController.setScene("/com/mordvinovdsw/library/hello-view.fxml", ScreenSizeConstants.MainControllerSize);
        stage.setTitle("Just Lib System");
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static void main(String[] args) {
        launch();
    }
}

