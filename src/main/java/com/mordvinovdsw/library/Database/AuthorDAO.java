package com.mordvinovdsw.library.Database;

import com.mordvinovdsw.library.models.Author;
import com.mordvinovdsw.library.utils.ErrorMessages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    public List<Author> fetchBookAuthors(int bookId) {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT a.Author_Id, a.Author_Name FROM Authors a "
                + "JOIN Book_Authors ba ON a.Author_Id = ba.Author_Id "
                + "WHERE ba.Book_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Author_Id");
                    String name = rs.getString("Author_Name");
                    authors.add(new Author(id, name));
                }
            }
        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }
        return authors;
    }
}
