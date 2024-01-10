package com.mordvinovdsw.library;

import com.mordvinovdsw.library.utils.SceneController;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final List<Stage> openStages = new ArrayList<>();
    private static SceneController sceneController;
    private static Stage primaryStage;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        sceneController = new SceneController(stage);
        sceneController.setScene("/com/mordvinovdsw/library/hello-view.fxml", ScreenSizeConstants.MainControllerSize);
        stage.setTitle("Just Lib System");
        openStages.add(stage);
        stage.show();
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void addStage(Stage stage) {
        openStages.add(stage);
    }
}

