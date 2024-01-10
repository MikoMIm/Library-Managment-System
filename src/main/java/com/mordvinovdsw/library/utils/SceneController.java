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
    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void setScene(String fxmlPath, double[] size) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, size[0], size[1]);
        stage.setScene(scene);
        stage.setWidth(size[0]);
        stage.setHeight(size[1]);
        stage.show();
    }
}