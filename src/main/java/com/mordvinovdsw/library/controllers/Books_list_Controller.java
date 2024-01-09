package com.mordvinovdsw.library.controllers;

import com.mordvinovdsw.library.models.Book;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.itemControllers.BookItemController;
import com.mordvinovdsw.library.supportControllers.EditBookController;
import com.mordvinovdsw.library.utils.ComboBoxUtil;
import com.mordvinovdsw.library.utils.ErrorMessages;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Books_list_Controller implements Initializable {
    public AnchorPane rootAnchorPane;
    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchComboBox;
    @FXML
    private ComboBox<String> sortComboBox;
    private List<Book> books;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboBoxUtil.fillBookSearchOptions(searchComboBox);
        books = getBooksFromDatabase();
        populateGridWithBooks(books);
        sortData();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction(newValue));
        searchComboBox.setOnAction(event -> handleSearchAction(searchTextField.getText()));
    }


    private List<Book> getBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT bl.Book_Id, bl.Book_Title, bl.Book_Numbers, bl.ISBN_10, bl.ISBN_13, "
                + "GROUP_CONCAT(DISTINCT g.Genre_Name) AS Genres, "
                + "GROUP_CONCAT(DISTINCT a.Author_Name) AS Authors "
                + "FROM Book_List bl "
                + "LEFT JOIN Book_Genres bg ON bl.Book_Id = bg.Book_Id "
                + "LEFT JOIN Genres g ON bg.Genre_Id = g.Genre_Id "
                + "LEFT JOIN Book_Authors ba ON bl.Book_Id = ba.Book_Id "
                + "LEFT JOIN Authors a ON ba.Author_Id = a.Author_Id "
                + "GROUP BY bl.Book_Id, bl.Book_Title, bl.Book_Numbers, bl.ISBN_10, bl.ISBN_13";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Book_Id");
                String title = rs.getString("Book_Title");
                int number = rs.getInt("Book_Numbers");
                String isbn10 = rs.getString("ISBN_10");
                String isbn13 = rs.getString("ISBN_13");
                String genres = rs.getString("Genres");
                String authors = rs.getString("Authors");

                books.add(new Book(id, title, number, isbn10, isbn13, genres, authors));
            }
        } catch (SQLException e) {
            ErrorMessages.showError("Database Error: " + e.getMessage());
        }
        return books;
    }

    private void populateGridWithBooks(List<Book> books) {
        gridPane.getChildren().clear();

        final int maxColumn = 3;
        int row = 0, column = 0;
        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/layouts/book_items.fxml"));
                VBox bookItemPane = loader.load();
                BookItemController itemController = loader.getController();
                itemController.setBook(book);

                gridPane.add(bookItemPane, column, row);
                column++;
                if (column == maxColumn) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                ErrorMessages.showError("IO Error: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Book.fxml"));
            Parent root = loader.load();
            EditBookController editController = loader.getController();
            editController.prepareAdd();
            Stage stage = new Stage();
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            ErrorMessages.showError("IO Error: " + e.getMessage());
        }
    }

    @FXML
    private void sortData() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Book ID", "Book Title", "Book Quantity"
        );
        sortComboBox.setItems(options);

        sortComboBox.setOnAction(event -> {
            String selectedOption = sortComboBox.getValue();
            switch (selectedOption) {
                case "Book ID":
                    books.sort(Comparator.comparingInt(Book::getBookID));
                    break;
                case "Book Title":
                    books.sort(Comparator.comparing(Book::getBookTitle));
                    break;
                case "Book Quantity":
                    books.sort(Comparator.comparingInt(Book::getBookNumber));
                    break;
            }
            populateGridWithBooks(books);
        });
    }

    @FXML
    private void handleSearchAction(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            populateGridWithBooks(books);
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        String searchCriterion = searchComboBox.getValue();

        List<Book> filteredBooks = books.stream()
                .filter(book -> {
                    if (searchCriterion == null || searchCriterion.isEmpty()) {
                        return true;
                    }
                    return switch (searchCriterion) {
                        case "Book ID" -> String.valueOf(book.getBookID()).contains(lowerCaseSearchText);
                        case "Book Name" -> book.getBookTitle().toLowerCase().contains(lowerCaseSearchText);
                        case "Book Genre" -> book.getGenre() != null && !book.getGenre().trim().isEmpty() &&
                                book.getGenre().toLowerCase().contains(lowerCaseSearchText);
                        case "Book ISBN" -> (book.getISBN10() != null && book.getISBN10().toLowerCase().contains(lowerCaseSearchText)) ||
                                (book.getISBN13() != null && book.getISBN13().toLowerCase().contains(lowerCaseSearchText));
                        case "Book Author" -> book.getAuthors() != null && !book.getAuthors().trim().isEmpty() &&
                                book.getAuthors().toLowerCase().contains(lowerCaseSearchText);
                        default -> true;
                    };
                })
                .collect(Collectors.toList());

        populateGridWithBooks(filteredBooks);
    }
}