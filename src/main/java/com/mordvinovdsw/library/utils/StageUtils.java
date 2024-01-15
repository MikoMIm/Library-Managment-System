package com.mordvinovdsw.library.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StageUtils {
    public static void centerStageOnScreen(Stage stage, Scene scene) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
    }

    public static void closeStageOf(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}