package com.mordvinovdsw.library.utils;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenUtil {
    public static void openLoginScreen() {
        Platform.runLater(() -> {
            try {
                Stage loginStage = new Stage();
                SceneController sceneController = new SceneController(loginStage);
                sceneController.setScene("/com/mordvinovdsw/library/hello-view.fxml", ScreenSizeConstants.SupportControllerSize);
                loginStage.setTitle("Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
