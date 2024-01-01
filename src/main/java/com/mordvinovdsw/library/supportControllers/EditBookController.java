package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.Database.*;

import com.mordvinovdsw.library.dataManager.BookDataManager;
import com.mordvinovdsw.library.dataManager.DataEntryManager;
import com.mordvinovdsw.library.models.Author;
import com.mordvinovdsw.library.models.Book;
import com.mordvinovdsw.library.models.BookData;
import com.mordvinovdsw.library.models.Genre;
import com.mordvinovdsw.library.dataManager.AuthorUtil;
import com.mordvinovdsw.library.utils.ErrorMessages;
import com.mordvinovdsw.library.dataManager.GenreUtil;
import com.mordvinovdsw.library.utils.GoogleBooksApiUtil;
import com.mordvinovdsw.library.utils.TextFieldLimitUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditBookController {

    @FXML
    private TextField bookTitleField, bookNumberField, ISBN10Field, ISBN13Field;
    @FXML
    private HBox genreComboBoxContainer, authorComboBoxContainer;
    @FXML
    private ComboBox<Genre> genreComboBox;
    @FXML
    private ComboBox<Author> authorComboBox;
    @FXML
    private Button removeGenreButton, addGenreButton, removeAuthorButton, addAuthorButton, addButton, saveButton, cancelButton;

    private List<ComboBox<Genre>> genreComboBoxes = new ArrayList<>();
    private List<ComboBox<Author>> authorComboBoxes = new ArrayList<>();
    private int currentBookId;
    private BookDataManager bookDataManager = new BookDataManager();
    private DataEntryManager dataEntryManager = new DataEntryManager();
    private AuthorDAO AuthorDAO = new AuthorDAO();

    private GenreDAO GenreDAO = new GenreDAO();

    @FXML
    private void initialize() {
        setupComboBoxes();
        setupTextFieldLimiters();
    }

    private void setupComboBoxes() {
        setupGenreComboBox(genreComboBox);
        setupAuthorComboBox(authorComboBox);
    }

    private void setupGenreComboBox(ComboBox<Genre> comboBox) {
        genreComboBoxes.add(comboBox);
        comboBox.setEditable(true);
        comboBox.setItems(GenreUtil.getGenresFromDatabase());
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Genre item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getGenreName());
            }
        });
        comboBox.getEditor().setOnAction(event -> handleNewGenreEntry(comboBox.getEditor().getText()));
    }

    private void setupAuthorComboBox(ComboBox<Author> comboBox) {
        authorComboBoxes.add(comboBox);
        comboBox.setEditable(true);
        comboBox.setItems(AuthorUtil.getAuthorsFromDatabase());
        comboBox.getEditor().setOnAction(event -> handleNewAuthorEntry(comboBox.getEditor().getText()));
    }

    private void setupTextFieldLimiters() {
        TextFieldLimitUtil.limitTextFieldLength(ISBN10Field, 10);
        TextFieldLimitUtil.limitTextFieldLength(ISBN13Field, 13);
    }

    private void handleNewAuthorEntry(String authorName) {
        Optional<Author> authorOpt = dataEntryManager.handleNewAuthorEntry(authorName);
        authorOpt.ifPresentOrElse(author -> {
            authorComboBox.getItems().add(author);
            authorComboBox.setValue(author);
        }, () -> {
        });
    }

    private void handleNewGenreEntry(String genreName) {
        Optional<Genre> genreOpt = dataEntryManager.handleNewGenreEntry(genreName);
        if (genreOpt.isPresent()) {
            Genre genre = genreOpt.get();
            genreComboBox.getItems().add(genre);
            genreComboBox.setValue(genre);
        }
    }

    @FXML
    private void addGenreComboBox() {
        if (genreComboBoxes.size() < 4) {
            ComboBox<Genre> newComboBox = new ComboBox<>();
            newComboBox.setEditable(true);
            newComboBox.setPrefWidth(genreComboBox.getPrefWidth());
            newComboBox.setPrefHeight(genreComboBox.getPrefHeight());
            newComboBox.setItems(genreComboBox.getItems());
            newComboBox.setCellFactory(genreComboBox.getCellFactory());

            genreComboBoxContainer.getChildren().add(newComboBox);
            genreComboBoxes.add(newComboBox);
            removeGenreButton.setDisable(false);
        }
        addGenreButton.setDisable(genreComboBoxes.size() >= 4);
    }

    @FXML
    private void removeGenreComboBox() {
        if (genreComboBoxes.size() > 1) {
            ComboBox<Genre> lastComboBox = genreComboBoxes.remove(genreComboBoxes.size() - 1);
            genreComboBoxContainer.getChildren().remove(lastComboBox);
            removeGenreButton.setDisable(genreComboBoxes.size() <= 1);
        }
        addGenreButton.setDisable(genreComboBoxes.size() >= 4);
    }

    @FXML
    private void addAuthorComboBox() {
        if (authorComboBoxes.size() < 3) {
            ComboBox<Author> newComboBox = new ComboBox<>();
            newComboBox.setEditable(true);
            newComboBox.setPrefWidth(authorComboBox.getPrefWidth());
            newComboBox.setPrefHeight(authorComboBox.getPrefHeight());
            newComboBox.setItems(authorComboBox.getItems());
            newComboBox.setCellFactory(authorComboBox.getCellFactory());
            authorComboBoxes.add(newComboBox);
            authorComboBoxContainer.getChildren().add(newComboBox);
            removeAuthorButton.setDisable(false);
        }
        addAuthorButton.setDisable(authorComboBoxes.size() >= 3);
    }

    @FXML
    private void removeAuthorComboBox() {
        if (authorComboBoxes.size() > 1) {
            ComboBox<Author> lastComboBox = authorComboBoxes.remove(authorComboBoxes.size() - 1);
            authorComboBoxContainer.getChildren().remove(lastComboBox);
            removeAuthorButton.setDisable(authorComboBoxes.size() <= 1);
        }
        addAuthorButton.setDisable(authorComboBoxes.size() >= 3);
    }

    public void prepareEdit(Book book) {
        fillForm(book);
        currentBookId = book.getBookID();
        loadBookGenres();
        loadBookAuthors();
        saveButton.setVisible(true);
        addButton.setVisible(false);
    }


    public void prepareAdd() {
        bookTitleField.clear();
        bookNumberField.clear();
        ISBN10Field.clear();
        ISBN13Field.clear();
        saveButton.setVisible(false);
        addButton.setVisible(true);
    }

    private void loadBookGenres() {
        List<Genre> bookGenres = GenreDAO.fetchBookGenres(currentBookId);
        if (!bookGenres.isEmpty()) {
            genreComboBox.setValue(bookGenres.get(0));
        }
        for (int i = 1; i < bookGenres.size(); i++) {
            addGenreComboBox();
            genreComboBoxes.get(i).setValue(bookGenres.get(i));
        }
    }

    private void loadBookAuthors() {
        List<Author> bookAuthors = AuthorDAO.fetchBookAuthors(currentBookId);
        if (!bookAuthors.isEmpty()) {
            authorComboBox.setValue(bookAuthors.get(0));
        }
        for (int i = 1; i < bookAuthors.size(); i++) {
            addAuthorComboBox();
            authorComboBoxes.get(i).setValue(bookAuthors.get(i));
        }
    }

    @FXML
    private void addData() {
        if (!validateInput()) {
            return;
        }
        try {
            int bookId = bookDataManager.insertBook(bookTitleField.getText(),
                    Integer.parseInt(bookNumberField.getText()),
                    ISBN10Field.getText(),
                    ISBN13Field.getText());

            bookDataManager.insertGenresForBook(bookId, genreComboBoxes);
            bookDataManager.insertAuthorsForBook(bookId, authorComboBoxes);
        } catch (SQLException | NumberFormatException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void saveData() {
        try {
            bookDataManager.updateBook(currentBookId, bookTitleField.getText(),
                    Integer.parseInt(bookNumberField.getText()), ISBN10Field.getText(), ISBN13Field.getText());

            List<Author> selectedAuthors = extractSelectedAuthors();
            bookDataManager.updateAuthorsForBook(currentBookId, selectedAuthors);

            List<Genre> selectedGenres = new ArrayList<>();
            for (ComboBox<Genre> genreComboBox : genreComboBoxes) {
                Object value = genreComboBox.getValue();
                if (value instanceof Genre) {
                    selectedGenres.add((Genre) value);
                } else if (value instanceof String) {
                    // Find the Genre by name or create a new one
                    String genreName = (String) value;
                    Genre genre = GenreUtil.findGenreByName(genreName);
                    if (genre == null) {
                        genre = GenreUtil.addNewGenreToDatabase(genreName);
                    }
                    selectedGenres.add(genre);
                }
            }
            bookDataManager.updateGenresForBook(currentBookId, selectedGenres);
        } catch (SQLException | NumberFormatException e) {
            ErrorMessages.showError("Database error: " + e.getMessage());
        } catch (ClassCastException e) {
            ErrorMessages.showError("Please select a valid genre.");
        }
    }

    private List<Author> extractSelectedAuthors() {
        List<Author> selectedAuthors = new ArrayList<>();
        for (ComboBox<Author> authorComboBox : authorComboBoxes) {
            Object selectedItem = authorComboBox.getValue();
            if (selectedItem instanceof Author) {
                selectedAuthors.add((Author) selectedItem);
            } else if (selectedItem instanceof String) {
                Optional<Author> authorOpt = dataEntryManager.handleNewAuthorEntry(selectedItem.toString());
                authorOpt.ifPresent(selectedAuthors::add);
            }
        }
        return selectedAuthors;
    }

    private List<Genre> extractSelectedGenres() {
        List<Genre> selectedGenres = new ArrayList<>();
        for (ComboBox<Genre> genreComboBox : genreComboBoxes) {
            Object selectedItem = genreComboBox.getValue();
            if (selectedItem instanceof Genre) {
                selectedGenres.add((Genre) selectedItem);
            } else if (selectedItem instanceof String) {
                Optional<Genre> genreOpt = dataEntryManager.handleNewGenreEntry(selectedItem.toString());
                if (genreOpt.isPresent()) {
                    selectedGenres.add(genreOpt.get());
                } else {
                    // Handle the case where no valid Genre is found or created
                }
            }
        }
        return selectedGenres;
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

    public void fillForm(Book book) {
        bookTitleField.setText(book.getBookTitle());
        bookNumberField.setText(String.valueOf(book.getBookNumber()));
        ISBN10Field.setText(book.getISBN10());
        ISBN13Field.setText(book.getISBN13());
    }


    @FXML
    private void autoFill() {
        String isbn = ISBN13Field.getText().trim(); // Assuming you're using ISBN13 for autofill
        if (isbn.isEmpty()) {
            ErrorMessages.showError("Please enter an ISBN number.");
            return;
        }

        try {
            BookData bookData = GoogleBooksApiUtil.fetchBookDataByISBN(isbn);
            if (bookData != null) {
                System.out.println("Title: " + bookData.getTitle());
                System.out.println("Authors: " + String.join(", ", bookData.getAuthors()));
                System.out.println("Genres: " + String.join(", ", bookData.getGenres()));
                System.out.println("ISBN-10: " + bookData.getIsbn10());
                System.out.println("ISBN-13: " + bookData.getIsbn13());

                bookTitleField.setText(bookData.getTitle());
                populateComboBoxes(bookData);

                ISBN10Field.setText(bookData.getIsbn10());
                ISBN13Field.setText(bookData.getIsbn13());
            } else {
                ErrorMessages.showError("No data found for this ISBN.");
            }
        } catch (Exception e) {
            ErrorMessages.showError("Error fetching data: " + e.getMessage());
        }
    }

    private void populateComboBoxes(BookData bookData) {
        genreComboBox.setValue(null);
        authorComboBox.setValue(null);

        clearAdditionalComboBoxes(genreComboBoxContainer, genreComboBoxes);
        clearAdditionalComboBoxes(authorComboBoxContainer, authorComboBoxes);

        Author firstAuthor = AuthorUtil.findAuthorByName(bookData.getAuthors().get(0));
        if (firstAuthor == null && !bookData.getAuthors().get(0).isEmpty()) {
            firstAuthor = AuthorUtil.addNewAuthorToDatabase(bookData.getAuthors().get(0));
        }
        authorComboBox.setValue(firstAuthor);

        Genre firstGenre = GenreUtil.findGenreByName(bookData.getGenres().get(0));
        if (firstGenre == null && !bookData.getGenres().get(0).isEmpty()) {
            firstGenre = GenreUtil.addNewGenreToDatabase(bookData.getGenres().get(0));
        }
        genreComboBox.setValue(firstGenre);

        for (int i = 1; i < bookData.getAuthors().size(); i++) {
            addAuthorComboBox();
            Author author = AuthorUtil.findAuthorByName(bookData.getAuthors().get(i));
            if (author == null) {
                author = AuthorUtil.addNewAuthorToDatabase(bookData.getAuthors().get(i));
            }
            authorComboBoxes.get(i).setValue(author);
        }

        for (int i = 1; i < bookData.getGenres().size(); i++) {
            addGenreComboBox();
            Genre genre = GenreUtil.findGenreByName(bookData.getGenres().get(i));
            if (genre == null) {
                genre = GenreUtil.addNewGenreToDatabase(bookData.getGenres().get(i));
            }
            genreComboBoxes.get(i).setValue(genre);
        }
    }
    private void clearAdditionalComboBoxes(HBox container, List<? extends ComboBox<?>> comboBoxes) {
        container.getChildren().removeIf(node -> container.getChildren().indexOf(node) != 0);
        comboBoxes.subList(1, comboBoxes.size()).clear();
    }
}
