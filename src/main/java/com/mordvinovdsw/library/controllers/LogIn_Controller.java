package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.AuthenticationService;
import com.mordvinovdsw.library.supportControllers.LoginUserController;
import com.mordvinovdsw.library.utils.ErrorMessages;
import com.mordvinovdsw.library.utils.StageUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class LogIn_Controller {

    public LogIn_Controller() {
        authService = new AuthenticationService();
    }
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField userID;
    @FXML
    private PasswordField password;
    private AuthenticationService authService;

    public void initialize() {
        if (!authService.doesUserExist()) {
            showLoginWarning();
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = userID.getText();
        String pwd = password.getText();

        if (username.isEmpty() || pwd.isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        } else if (authService.validateCredentials(username, pwd)) {
            wrongLogin.setText("Success!");
            navigateToMainScene();
        } else {
            wrongLogin.setText("Wrong Login or password.");
        }
    }

    private void navigateToMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/MainMenu.fxml"));
            Parent mainSceneRoot = loader.load();
            Stage currentStage = (Stage) userID.getScene().getWindow();
            Scene mainScene = new Scene(mainSceneRoot);
            currentStage.setScene(mainScene);
            StageUtils.centerStageOnScreen(currentStage, mainScene);

            currentStage.setTitle("Main Menu");
            currentStage.show();
        } catch (IOException e) {
            ErrorMessages.showError("Failed to load the main scene.");
            wrongLogin.setText("Failed to load the main scene.");
        }
    }


    private void showLoginWarning() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Login_Warning.fxml"));
            Parent root = loader.load();

            LoginUserController warningController = loader.getController();
            warningController.prepareAdd();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Required");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnCloseRequest(Event::consume);
            stage.showAndWait();
        } catch (IOException e) {
            ErrorMessages.showError("Failed to display login warning.");
            e.printStackTrace();
        }
    }
}


