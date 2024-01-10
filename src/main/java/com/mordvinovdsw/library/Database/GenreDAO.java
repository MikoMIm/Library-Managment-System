package com.mordvinovdsw.library.Database;

import com.mordvinovdsw.library.models.Genre;
import com.mordvinovdsw.library.utils.DialogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {
    public List<Genre> fetchBookGenres(int bookId) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.Genre_Id, g.Genre_Name FROM Genres g " +
                "JOIN Book_Genres bg ON g.Genre_Id = bg.Genre_Id " +
                "WHERE bg.Book_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Genre_Id");
                    String name = rs.getString("Genre_Name");
                    genres.add(new Genre(id, name));
                }
            }
        } catch (SQLException e) {
            DialogUtil.showError("Database error: " + e.getMessage());
        }
        return genres;
    }
}
