package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.supportControllers.LoginUserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginWarning {
    public void showLoginWarning() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Login_Warning.fxml"));
            Parent root = loader.load();
            LoginUserController warningController = loader.getController();
            warningController.prepareFirstAdd();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Required");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            DialogUtil.showError("Failed to display login warning.");
            e.printStackTrace();
        }
    }
}
