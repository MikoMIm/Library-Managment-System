package com.mordvinovdsw.library;

import com.mordvinovdsw.library.utils.SceneController;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.application.Application;
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
        launch(args);
    }
}
