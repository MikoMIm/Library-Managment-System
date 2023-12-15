package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.Database.Book;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Book_list_Controller implements Initializable{

    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, Integer>  bookIDColumn ;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn ;
    @FXML
    private TableColumn<Book, Integer> bookNumberColumn ;

    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField bookNumberField;

    private ObservableList<Book> books = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        bookNumberColumn.setCellValueFactory(new PropertyValueFactory<>("bookNumber"));
        loadData();
    }

    private void loadData() {
        books.clear();
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Book_list")) {

            while (resultSet.next()) {
                int bookID = resultSet.getInt("Book_id");
                String bookTitle = resultSet.getString("Book_Title");
                double price = resultSet.getDouble("Price");
                int bookNumber = resultSet.getInt("Book_Numbers");

                books.add(new Book(bookID, bookTitle, price, bookNumber));
                clearFields();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        tableView.setItems(books);
    }

    @FXML
    private void exit() throws IOException {
        Main.changeScene();

    }
    @FXML
    private void addData() {
        try (Connection connection = DBConnection.getConnection()) {
            String maxIdQuery = "SELECT MAX(Book_id) AS max_id FROM Book_list";
            int bookId = 1;

            try (PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery)) {
                ResultSet resultSet = maxIdStatement.executeQuery();
                if (resultSet.next()) {
                    int maxId = resultSet.getInt("max_id");
                    if (!resultSet.wasNull()) {
                        bookId = maxId + 1;
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Book_list(Book_id, Book_Title, Price, Book_Numbers) VALUES (?, ?, ?, ?)")) {
                statement.setInt(1, bookId);
                statement.setString(2, bookTitleField.getText());
                statement.setDouble(3, Double.parseDouble(priceField.getText()));
                statement.setInt(4, Integer.parseInt(bookNumberField.getText()));
                statement.executeUpdate();
                loadData();
                clearFields();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    @FXML
    private void updateData() {
        String bookTitle = bookTitleField.getText();
        double price = Double.parseDouble(priceField.getText());
        int bookNumber = Integer.parseInt(bookNumberField.getText());

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Book_list SET Book_Title = ?, Price = ?, Book_Numbers = ? WHERE Book_id = ?")) {

            statement.setString(1, bookTitle);
            statement.setDouble(2, price);
            statement.setInt(3, bookNumber);

            int result = statement.executeUpdate();
            if (result == 1) {
                loadData();
                clearFields();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        bookTitleField.clear();
        priceField.clear();
        bookNumberField.clear();
    }

    @FXML
    private void clearFieldsb() {
        bookTitleField.clear();
        priceField.clear();
        bookNumberField.clear();
    }
    @FXML
    private void deleteData() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Book_list WHERE Book_id = ?")) {

            Book selectedBook = tableView.getSelectionModel().getSelectedItem();
            statement.setInt(1, selectedBook.getBookID());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        loadData();
    }



    @FXML
    private void MCK() {

        tableView.setOnMouseClicked(event -> {
            Book selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                bookTitleField.setText(selectedBook.getBookTitle());
                priceField.setText(String.valueOf(selectedBook.getPrice()));
                bookNumberField.setText(String.valueOf(selectedBook.getBookNumber()));
            }
        });

    }


}




