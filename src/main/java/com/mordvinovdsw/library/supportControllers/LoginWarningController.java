package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginWarningController {
    @FXML
    private TextField LoginField;

    @FXML
    private TextField PasswordField;

    @FXML
    private Label headerLabel;

    @FXML
    private void createUser(ActionEvent event) {
        String username = LoginField.getText().trim();
        String password = PasswordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            headerLabel.setText("Username and password cannot be empty.");
            return;
        }
        String encryptedPassword = encryptPassword(password);
        if (addUserToDatabase(username, encryptedPassword)) {
            headerLabel.setText("Administrator created successfully.");
            closeStage(event);
        } else {
            headerLabel.setText("Failed to create administrator.");
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    private boolean addUserToDatabase(String username, String encryptedPassword) {
        String sql = "INSERT INTO Users(Login, Password) VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            headerLabel.setText("Database error: " + e.getMessage());
            return false;
        }
    }
}