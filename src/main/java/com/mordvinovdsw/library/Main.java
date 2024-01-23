package com.mordvinovdsw.library;

import com.mordvinovdsw.library.utils.SceneController;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    private static SceneController sceneController;
    private static Image applicationIcon;

    @Override
    public void start(Stage stage) throws IOException {
        applicationIcon = new Image(getClass().getResource("/IMG/Icon.png").toExternalForm());

        sceneController = new SceneController(stage);
        sceneController.setScene("/com/mordvinovdsw/library/hello-view.fxml", ScreenSizeConstants.MainControllerSize);
        stage.setTitle("Just Lib System");
        stage.getIcons().add(applicationIcon);
    }
    public static Stage createStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(applicationIcon);
        stage.setScene(new Scene(root));
        return stage;
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
