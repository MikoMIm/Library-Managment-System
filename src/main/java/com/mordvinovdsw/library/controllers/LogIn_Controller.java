package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.AuthenticationService;
import com.mordvinovdsw.library.utils.DatabaseUtils;
import com.mordvinovdsw.library.utils.DialogUtil;
import com.mordvinovdsw.library.utils.LoginWarning;
import com.mordvinovdsw.library.utils.StageUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LogIn_Controller {
    private static final Logger LOGGER = Logger.getLogger(LogIn_Controller.class.getName());
    public LogIn_Controller() {
        authService = new AuthenticationService();
    }
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField userID;
    @FXML
    private PasswordField password;
    private final AuthenticationService authService;

    public void initialize() {
        System.out.println("Looking for DB at: " + Paths.get("library.db").toAbsolutePath());

        if (!DatabaseUtils.doesDatabaseExist("library.db")) {
            DialogUtil.showDialog("Database not found", "Database not found. Import or create a database.");
            openImportExportScene();
        } else {
            if (!authService.doesUserExist()) {
                LoginWarning loginWarning = new LoginWarning();
                loginWarning.showLoginWarning();
            }
        }
    }


    private void openImportExportScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/ImportExport_layout.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Import or Create Database");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            DialogUtil.showError("Failed to open the Import/Export scene.");
            LOGGER.log(Level.SEVERE, "Failed to open the Import/Export scene: ", e);
        }
    }

    @FXML
    public void handleLogin() {
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
            DialogUtil.showError("Failed to load the main scene.");
            wrongLogin.setText("Failed to load the main scene.");
        }
    }
}


