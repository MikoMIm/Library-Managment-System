package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.Database.Book;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.Genre;
import com.mordvinovdsw.library.utils.ErrorMessages;
import com.mordvinovdsw.library.utils.GenreUtil;
import com.mordvinovdsw.library.utils.TextFieldLimitUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditBookController {

    @FXML
    private TextField bookTitleField;
    @FXML
    private HBox  mainContainer1;
    @FXML
    private ComboBox<Genre> genreComboBox1;
    @FXML
    private Button removeGenreButton;
    @FXML
    private Button addGenreButton;
    @FXML
    private TextField bookNumberField;
    @FXML
    private TextField ISBN10Field;
    @FXML
    private TextField ISBN13Field;
    @FXML
    private Button addButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private List<ComboBox<Genre>> genreComboBoxes = new ArrayList<>();
    private int currentBookId;



    public void initialize() {
        genreComboBoxes.add(genreComboBox1);
        initializeGenreComboBox(genreComboBox1);

        List<Genre> genres = GenreUtil.getGenresFromDatabase();
        genres.sort(Comparator.comparing(Genre::getGenreName));
        genreComboBox1.setItems(FXCollections.observableArrayList(genres));


        genreComboBox1.setCellFactory(new Callback<ListView<Genre>, ListCell<Genre>>() {
            @Override
            public ListCell<Genre> call(ListView<Genre> param) {
                return new ListCell<Genre>() {
                    @Override
                    protected void updateItem(Genre item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.getGenreName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        TextFieldLimitUtil.limitTextFieldLength(ISBN10Field, 10);
        TextFieldLimitUtil.limitTextFieldLength(ISBN13Field, 13);
    }

    private void initializeGenreComboBox(ComboBox<Genre> comboBox) {
        List<Genre> genres = GenreUtil.getGenresFromDatabase();
        comboBox.setItems(FXCollections.observableArrayList(genres));
    }
    public void prepareEdit(Book book) {
        fillForm(book);
        currentBookId = book.getBookID();
        List<Genre> bookGenres = fetchBookGenres(currentBookId);
        if (!bookGenres.isEmpty()) {
            genreComboBox1.setValue(bookGenres.get(0));
        }
        for (int i = 1; i < bookGenres.size(); i++) {
            addGenreComboBox();
            genreComboBoxes.get(i).setValue(bookGenres.get(i));
        }
        saveButton.setVisible(true);
        addButton.setVisible(false);
    }

    private List<Genre> fetchBookGenres(int bookId) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.Genre_Id, g.Genre_Name FROM Genres g "
                + "JOIN Book_Genres bg ON g.Genre_Id = bg.Genre_Id "
                + "WHERE bg.Book_Id = ?";

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
            ErrorMessages.showError("Database error: " + e.getMessage());
        }
        return genres;
    }

    @FXML
    private void removeGenreComboBox() {
        if (genreComboBoxes.size() > 1) {
            ComboBox<Genre> lastComboBox = genreComboBoxes.remove(genreComboBoxes.size() - 1);
            mainContainer1.getChildren().remove(lastComboBox);
            removeGenreButton.setDisable(genreComboBoxes.size() <= 1);
        }
        addGenreButton.setDisable(genreComboBoxes.size() >= 4);
    }
    public void prepareAdd() {
        clearForm();
        saveButton.setVisible(false);
        addButton.setVisible(true);
    }

    private void clearForm() {
        bookTitleField.clear();
        bookNumberField.clear();
        ISBN10Field.clear();
        ISBN13Field.clear();
    }
    public void fillForm(Book book) {
        bookTitleField.setText(book.getBookTitle());
        bookNumberField.setText(String.valueOf(book.getBookNumber()));
        ISBN10Field.setText(book.getISBN10());
        ISBN13Field.setText(book.getISBN13());
    }
    @FXML
    private void addGenreComboBox() {
        if (genreComboBoxes.size() < 4) {
            ComboBox<Genre> newComboBox = new ComboBox<>();
            initializeGenreComboBox(newComboBox);

            newComboBox.setPrefWidth(genreComboBox1.getPrefWidth());
            newComboBox.setPrefHeight(genreComboBox1.getPrefHeight());

            mainContainer1.getChildren().add(newComboBox);
            genreComboBoxes.add(newComboBox);

            removeGenreButton.setDisable(false);
        }

        addGenreButton.setDisable(genreComboBoxes.size() >= 4);
    }




    @FXML
    private void addData() {
        if (!validateInput()) {
            return;
        }

        String sqlInsertBook = "INSERT INTO Book_List (Book_Title, Book_Numbers, ISBN_10, ISBN_13) VALUES (?, ?, ?, ?)";
        String sqlInsertGenre = "INSERT INTO Book_Genres (Book_id, Genre_Id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtInsertBook = conn.prepareStatement(sqlInsertBook, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtInsertGenre = conn.prepareStatement(sqlInsertGenre)) {

            // Insert the book details
            pstmtInsertBook.setString(1, bookTitleField.getText());
            pstmtInsertBook.setInt(2, Integer.parseInt(bookNumberField.getText()));
            pstmtInsertBook.setString(3, ISBN10Field.getText());
            pstmtInsertBook.setString(4, ISBN13Field.getText());
            int affectedRows = pstmtInsertBook.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }


            try (ResultSet generatedKeys = pstmtInsertBook.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int lastInsertedId = generatedKeys.getInt(1);


                    for (ComboBox<Genre> genreComboBox : genreComboBoxes) {
                        Genre selectedGenre = genreComboBox.getValue();
                        if (selectedGenre != null) {
                            pstmtInsertGenre.setInt(1, lastInsertedId);
                            pstmtInsertGenre.setInt(2, selectedGenre.getGenreID());
                            pstmtInsertGenre.executeUpdate();
                        }
                    }
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ErrorMessages.showError("Invalid input format: " + e.getMessage());
        }
    }

    @FXML
    private void saveData() {
        String sqlUpdateBook = "UPDATE Book_List SET Book_Title = ?, Book_numbers = ?, ISBN_10 = ?, ISBN_13 = ? WHERE Book_Id = ?";
        String sqlDeleteGenres = "DELETE FROM Book_Genres WHERE Book_id = ?";
        String sqlInsertGenre = "INSERT INTO Book_Genres (Book_id, Genre_Id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtUpdateBook = conn.prepareStatement(sqlUpdateBook);
             PreparedStatement pstmtDeleteGenres = conn.prepareStatement(sqlDeleteGenres);
             PreparedStatement pstmtInsertGenre = conn.prepareStatement(sqlInsertGenre)) {


            pstmtUpdateBook.setString(1, bookTitleField.getText());
            pstmtUpdateBook.setInt(2, Integer.parseInt(bookNumberField.getText()));
            pstmtUpdateBook.setString(3, ISBN10Field.getText());
            pstmtUpdateBook.setString(4, ISBN13Field.getText());
            pstmtUpdateBook.setInt(5, currentBookId);
            pstmtUpdateBook.executeUpdate();

            pstmtDeleteGenres.setInt(1, currentBookId);
            pstmtDeleteGenres.executeUpdate();

            for (ComboBox<Genre> genreComboBox : genreComboBoxes) {
                Genre selectedGenre = genreComboBox.getValue();
                if (selectedGenre != null) {
                    pstmtInsertGenre.setInt(1, currentBookId);
                    pstmtInsertGenre.setInt(2, selectedGenre.getGenreID());
                    pstmtInsertGenre.executeUpdate();
                }
            }

        } catch (SQLException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ErrorMessages.showError("Invalid input format: " + e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    private boolean validateInput() {

        if (bookTitleField.getText().trim().isEmpty() ||
                bookNumberField.getText().trim().isEmpty() ||
                ISBN10Field.getText().trim().isEmpty() ||
                ISBN13Field.getText().trim().isEmpty()) {

            ErrorMessages.showError("All fields are required.");
            return false;
        }

        try {
            Integer.parseInt(bookNumberField.getText().trim());
        } catch (NumberFormatException e) {
            ErrorMessages.showError("Book Numbers must be valid numbers.");
            return false;
        }

        return true;
    }

}
