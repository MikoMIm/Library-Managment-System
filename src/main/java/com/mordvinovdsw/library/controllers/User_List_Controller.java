package com.mordvinovdsw.library.controllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.itemControllers.UserItemController;
import com.mordvinovdsw.library.models.User;
import com.mordvinovdsw.library.supportControllers.EditMemberController;
import com.mordvinovdsw.library.supportControllers.LoginUserController;
import com.mordvinovdsw.library.utils.DialogUtil;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User_List_Controller implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(User_List_Controller.class.getName());
    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<User> administrators = getUsersFromDatabase();
        populateGridWithUsers(administrators);
    }

    public List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT Id, login, password FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(rs.getInt("Id"), rs.getString("login"), rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: ", e);
            DialogUtil.showError("Database error: " + e.getMessage());
        }

        return users;
    }

    public void populateGridWithUsers(List<User> users) {
        gridPane.getChildren().clear();

        final int maxColumn = 3;
        int row = 0, column = 0;
        for (User user : users) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/layouts/user_items.fxml"));
                Node userItem = loader.load();
                UserItemController itemController = loader.getController();
                itemController.setUser(user);

                gridPane.add(userItem, column, row);
                column++;
                if (column == maxColumn) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IO Error: ", e);
                DialogUtil.showError("IO Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exit() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/MainMenu.fxml", ScreenSizeConstants.MainControllerSize);
    }

    @FXML
    private void addNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Member.fxml"));
            Parent root = loader.load();
            EditMemberController editController = loader.getController();
            editController.prepareAdd();
            editController.setRefreshCallback(this::refreshGrid);
            Stage stage = Main.createStage("Add New Member", root);
            stage.show();
        } catch (IOException e) {
            DialogUtil.showError("IO Error: " + e.getMessage());
        }
    }
    @FXML
    private void refreshGrid() {
        List<User> administrators = getUsersFromDatabase();
        populateGridWithUsers(administrators);
        LOGGER.log(Level.INFO, "User grid has been refreshed.");
    }
}


