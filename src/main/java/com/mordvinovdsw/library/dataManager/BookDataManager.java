package com.mordvinovdsw.library.dataManager;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.Author;
import com.mordvinovdsw.library.models.Book;
import com.mordvinovdsw.library.models.Genre;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.List;

public class BookDataManager {
    public int insertBook(String title, int number, String isbn10, String isbn13) throws SQLException {
        String sqlInsertBook = "INSERT INTO Book_List (Book_Title, Book_Numbers, ISBN_10, ISBN_13) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertBook, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, title);
            pstmt.setInt(2, number);
            pstmt.setString(3, isbn10);
            pstmt.setString(4, isbn13);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        }
    }

    public void updateBook(int bookId, String title, int number, String isbn10, String isbn13) throws SQLException {
        String sql = "UPDATE Book_List SET Book_Title = ?, Book_Numbers = ?, ISBN_10 = ?, ISBN_13 = ? WHERE Book_Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setInt(2, number);
            pstmt.setString(3, isbn10);
            pstmt.setString(4, isbn13);
            pstmt.setInt(5, bookId);
            pstmt.executeUpdate();
        }
    }

    public void insertGenresForBook(int bookId, List<ComboBox<Genre>> genreComboBoxes) {
        String sqlInsertGenre = "INSERT INTO Book_Genres (Book_Id, Genre_Id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertGenre)) {
            for (ComboBox<Genre> genreComboBox : genreComboBoxes) {
                Object value = genreComboBox.getValue();
                Genre selectedGenre = null;
                if (value instanceof Genre) {
                    selectedGenre = (Genre) value;
                } else if (value instanceof String) {
                    String genreName = (String) value;
                    selectedGenre = GenreUtil.findGenreByName(genreName);
                    if (selectedGenre == null) {
                        selectedGenre = GenreUtil.addNewGenreToDatabase(genreName);
                    }
                }
                if (selectedGenre != null) {
                    pstmt.setInt(1, bookId);
                    pstmt.setInt(2, selectedGenre.getGenreID());
                    pstmt.executeUpdate();
                } else {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAuthorsForBook(int bookId, List<ComboBox<Author>> authorComboBoxes) throws SQLException {
        String sqlInsertAuthor = "INSERT INTO Book_Authors (Book_Id, Author_Id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertAuthor)) {
            for (ComboBox<Author> authorComboBox : authorComboBoxes) {
                Object value = authorComboBox.getValue();
                Author selectedAuthor = null;
                if (value instanceof Author) {
                    selectedAuthor = (Author) value;
                } else if (value instanceof String) {
                    String authorName = (String) value;
                    selectedAuthor = AuthorUtil.findAuthorByName(authorName);
                    if (selectedAuthor == null) {
                        selectedAuthor = AuthorUtil.addNewAuthorToDatabase(authorName);
                    }
                }

                // Now insert the author if it's not null
                if (selectedAuthor != null) {
                    pstmt.setInt(1, bookId);
                    pstmt.setInt(2, selectedAuthor.getAuthorID());
                    pstmt.executeUpdate();
                }
            }
        }
    }


    public void updateGenresForBook(int bookId, List<Genre> genres) throws SQLException {
        String deleteSql = "DELETE FROM Book_Genres WHERE Book_Id = ?";
        String insertSql = "INSERT INTO Book_Genres (Book_Id, Genre_Id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
            pstmtDelete.setInt(1, bookId);
            pstmtDelete.executeUpdate();
            for (Genre genre : genres) {
                pstmtInsert.setInt(1, bookId);
                pstmtInsert.setInt(2, genre.getGenreID());
                pstmtInsert.executeUpdate();
            }
        }
    }

    public void updateAuthorsForBook(int bookId, List<Author> authors) throws SQLException {
        String deleteSql = "DELETE FROM Book_Authors WHERE Book_Id = ?";
        String insertSql = "INSERT INTO Book_Authors (Book_Id, Author_Id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
            pstmtDelete.setInt(1, bookId);
            pstmtDelete.executeUpdate();
            for (Author author : authors) {
                pstmtInsert.setInt(1, bookId);
                pstmtInsert.setInt(2, author.getAuthorID());
                pstmtInsert.executeUpdate();
            }
        }
    }
}


