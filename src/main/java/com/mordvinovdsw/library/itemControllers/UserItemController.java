package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.models.User;
import com.mordvinovdsw.library.supportControllers.LoginUserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UserItemController {
    @FXML
    private VBox rootVBox;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button edit;
    @FXML
    private Button remove;

    private User user;


    public void setUser(User administrator) {
        this.user = administrator;
        updateUI();
    }

    private void updateUI() {
        idLabel.setText("Administrator ID - " + user.getId());
        nameLabel.setText("Administrator Login - " + user.getLogin());
    }

    @FXML
    private void openEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Login_Warning.fxml"));
            Parent editRoot = loader.load();

            LoginUserController editController = loader.getController();
            editController.prepareEdit(this.user);

            Scene editScene = new Scene(editRoot);
            Stage editStage = new Stage();
            editStage.setScene(editScene);
            editStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeData() {

    }
}

