package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.Genre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
