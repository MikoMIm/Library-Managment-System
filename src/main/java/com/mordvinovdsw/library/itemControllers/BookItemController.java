package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.models.Book;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.supportControllers.EditBookController;
import com.mordvinovdsw.library.utils.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookItemController {
    private Book book;

    @FXML
    private Label idLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label ISBNLabel13;
    @FXML
    private Label ISBNLabel10;
    @FXML
    private Label genreLabel;
    @FXML
    private Label authorLabel;


    public void setBook(Book book) {
        this.book = book;
        idLabel.setText("Book ID: " + book.getBookID());
        ISBNLabel13.setText("ISBN-13: " + book.getISBN13());
        ISBNLabel10.setText("ISBN-10: " + book.getISBN10());
        titleLabel.setText("Book Title: " + book.getBookTitle());
        quantityLabel.setText("Number of Books: " + book.getBookNumber());
        genreLabel.setText("Genre: " + book.getGenre());
        authorLabel.setText("Author: " + book.getAuthors());
    }


    @FXML
    private void openEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Book.fxml"));
            Parent root = loader.load();
            EditBookController editController = loader.getController();
            editController.fillForm(this.book);
            editController.prepareEdit(this.book);
            Stage stage = new Stage();
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeData() {
        String deleteBookGenresSql = "DELETE FROM Book_Genres WHERE Book_Id = ?";
        String deleteBookAuthorsSql = "DELETE FROM Book_Authors WHERE Book_Id = ?";
        String deleteBookSql = "DELETE FROM Book_List WHERE Book_Id = ?";
        Connection conn = null;
        PreparedStatement pstmtDeleteBookGenres = null;
        PreparedStatement pstmtDeleteBookAuthors = null;
        PreparedStatement pstmtDeleteBook = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Delete book genres
            pstmtDeleteBookGenres = conn.prepareStatement(deleteBookGenresSql);
            pstmtDeleteBookGenres.setInt(1, book.getBookID());
            pstmtDeleteBookGenres.executeUpdate();

            // Delete book authors
            pstmtDeleteBookAuthors = conn.prepareStatement(deleteBookAuthorsSql);
            pstmtDeleteBookAuthors.setInt(1, book.getBookID());
            pstmtDeleteBookAuthors.executeUpdate();

            // Delete the book itself
            pstmtDeleteBook = conn.prepareStatement(deleteBookSql);
            pstmtDeleteBook.setInt(1, book.getBookID());
            pstmtDeleteBook.executeUpdate();

            // Commit all changes
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    DialogUtil.showError("Error rolling back: " + ex.getMessage());
                }
            }
            DialogUtil.showError("Database error: " + e.getMessage());
        } finally {
            try {
                if (pstmtDeleteBookGenres != null) pstmtDeleteBookGenres.close();
                if (pstmtDeleteBookAuthors != null) pstmtDeleteBookAuthors.close();
                if (pstmtDeleteBook != null) pstmtDeleteBook.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                DialogUtil.showError("Error closing connections: " + ex.getMessage());
            }
        }
    }
}