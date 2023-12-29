package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.Genre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GenreUtil {
    public static ObservableList<Genre> getGenresFromDatabase() {
        ObservableList<Genre> genres = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Genres";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Genre genre = new Genre(rs.getInt("Genre_Id"), rs.getString("Genre_Name"));
                genres.add(genre);
            }

        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }

        return genres;
    }

    public static int addNewGenreToDatabase(String genreName) {
        String sql = "INSERT INTO Genres (Genre_Name) VALUES (?)";
        int genreId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, genreName);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genreId = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
            return -1;
        }

        return genreId;
    }
}

