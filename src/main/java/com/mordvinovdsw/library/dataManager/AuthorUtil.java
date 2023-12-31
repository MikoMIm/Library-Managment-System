package com.mordvinovdsw.library.dataManager;

import com.mordvinovdsw.library.models.Author;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.utils.ErrorMessages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AuthorUtil {
    public static ObservableList<Author> getAuthorsFromDatabase() {
        ObservableList<Author> authors = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Authors";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Author author = new Author(rs.getInt("Author_Id"), rs.getString("Author_Name"));
                authors.add(author);
            }

        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }

        return authors;
    }

    public static Author findAuthorByName(String name) {
        String sql = "SELECT * FROM Authors WHERE Author_Name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(rs.getInt("Author_Id"), rs.getString("Author_Name"));
                }
            }
        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }
        return null;
    }

    public static Author addNewAuthorToDatabase(String name) {
        String sql = "INSERT INTO Authors (Author_Name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        return new Author(newId, name);
                    }
                }
            }
        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }
        return null;
    }
}