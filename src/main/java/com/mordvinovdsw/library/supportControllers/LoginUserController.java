package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginUserController {
    private static final Logger LOGGER = Logger.getLogger(LoginUserController.class.getName());
    public AnchorPane rootAnchorPane;
    public VBox vboxLayout;
    @FXML
    private TextField LoginField;

    @FXML
    private TextField PasswordField;

    @FXML
    private Button CreateButton;

    @FXML
    private Button SaveButton;
    @FXML
    private Label headerLabel;

    private User currentUser;
    private boolean isEditMode = false;

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
            LOGGER.log(Level.SEVERE, "Database error: ", e);
            headerLabel.setText("Database error: " + e.getMessage());
            return false;
        }
    }

    public void prepareFirstAdd() {
        LoginField.setText("");
        PasswordField.setText("");
        CreateButton.setVisible(true);
        SaveButton.setVisible(false);
    }
    public void prepareAdd() {
        LoginField.setText("");
        PasswordField.setText("");
        headerLabel.setText("");
        CreateButton.setVisible(true);
        SaveButton.setVisible(false);
    }

    public void prepareEdit(User user) {
        isEditMode = true;
        this.currentUser = user;
        LoginField.setText(user.getLogin());
        headerLabel.setText("");
        CreateButton.setVisible(false);
        SaveButton.setVisible(true);
    }

    @FXML
    private void saveData(ActionEvent event) {
        if (!isEditMode || currentUser == null) {
            headerLabel.setText("Invalid operation.");
            return;
        }

        String username = LoginField.getText().trim();
        String password = PasswordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            headerLabel.setText("Username and password cannot be empty.");
            return;
        }

        String encryptedPassword = encryptPassword(password);
        if (updateUserInDatabase(currentUser.getId(), username, encryptedPassword)) {
            headerLabel.setText("Administrator updated successfully.");
            closeStage(event);
        } else {
            headerLabel.setText("Failed to update administrator.");
        }
    }

    private boolean updateUserInDatabase(int userId, String username, String encryptedPassword) {
        String sql = "UPDATE Users SET Login = ?, Password = ? WHERE Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.setInt(3, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: ", e);
            headerLabel.setText("Database error: " + e.getMessage());
            return false;
        }
    }

}