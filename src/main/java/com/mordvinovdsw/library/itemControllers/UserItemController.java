package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.models.User;
import com.mordvinovdsw.library.supportControllers.LoginUserController;
import com.mordvinovdsw.library.utils.IssueStatusChecker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserItemController {
    private static final Logger LOGGER = Logger.getLogger(IssueStatusChecker.class.getName());
    public Label statusLabel;
    public Button edit;
    public Button remove;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;


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
            Stage editStage = Main.createStage("Edit User", editRoot);
            editStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening edit window for User ID: " + user.getId(), e);
        }
    }

    @FXML
    private void removeData() {
        String deleteSql = "DELETE FROM users WHERE Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, user.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "User successfully removed with ID: " + user.getId());
            } else {
                LOGGER.log(Level.WARNING, "No user found with ID: " + user.getId());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in removeData for User ID: " + user.getId(), e);
        }
    }
}


