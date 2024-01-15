package com.mordvinovdsw.library.Database;

import com.mordvinovdsw.library.models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    public Book fetchBookById(int bookId) {
        String sql = "SELECT bl.Book_Id, bl.Book_Title, bl.Book_Numbers, bl.ISBN_10, bl.ISBN_13, GROUP_CONCAT(DISTINCT g.Genre_Name) AS Genres, GROUP_CONCAT(DISTINCT a.Author_Name) AS Authors FROM Book_List bl LEFT JOIN Book_Genres bg ON bl.Book_Id = bg.Book_Id LEFT JOIN Genres g ON bg.Genre_Id = g.Genre_Id LEFT JOIN Book_Authors ba ON bl.Book_Id = ba.Book_Id LEFT JOIN Authors a ON ba.Author_Id = a.Author_Id WHERE bl.Book_Id = ? GROUP BY bl.Book_Id, bl.Book_Title, bl.Book_Numbers, bl.ISBN_10, bl.ISBN_13;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("Book_Id");
                String title = rs.getString("Book_Title");
                int number = rs.getInt("Book_Numbers");
                String isbn10 = rs.getString("ISBN_10");
                String isbn13 = rs.getString("ISBN_13");
                String genres = rs.getString("Genres");
                String authors = rs.getString("Authors");

                return new Book(id, title, number, isbn10, isbn13, genres, authors);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
