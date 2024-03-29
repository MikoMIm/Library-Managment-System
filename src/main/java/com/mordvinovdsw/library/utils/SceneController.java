package com.mordvinovdsw.library.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneController {

    private final Stage stage;

    public Stage getStage() {
        return stage;
    }
    public void setStageTitle(String title) {
        stage.setTitle(title);
    }
    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void setScene(String fxmlPath, double[] size) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ScreenSizeConstants.adjustStageSize(stage, size);
        stage.show();
    }
}