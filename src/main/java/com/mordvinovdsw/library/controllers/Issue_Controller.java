package com.mordvinovdsw.library.controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.io.IOException;

import com.mordvinovdsw.library.Database.Book;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.Issue;
import com.mordvinovdsw.library.Database.Member;
import com.mordvinovdsw.library.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Issue_Controller implements Initializable {

    @FXML
    private TableView<Issue> IssueView;
    @FXML
    private TableColumn<Issue, Integer> IDIssueBook;
    @FXML
    private TableColumn<Issue, Integer> ID_book;
    @FXML
    private TableColumn<Issue, Integer> ID_Member;
    @FXML
    private TableColumn<Issue, String> DateE;
    @FXML
    private TableColumn<Issue, String> DateR;
    @FXML
    private TableColumn<Issue, String> status;
    @FXML
    private ComboBox<Book> bookIdCombo;
    @FXML
    private ComboBox<Member> memberIdCombo;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private ComboBox<String> statusCombo;

    private ObservableList<Issue> issueData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusCombo.getItems().add("Active");
        statusCombo.getItems().add("Inactive");
        initializeTableColumns();
        loadData();
        loadBooks();
        loadMembers();
    }



    private void initializeTableColumns() {
        IDIssueBook.setCellValueFactory(new PropertyValueFactory<>("issueID"));
        ID_book.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        ID_Member.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        DateE.setCellValueFactory(new PropertyValueFactory<>("dateIssue"));
        DateR.setCellValueFactory(new PropertyValueFactory<>("dateReturn"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Book_Issue";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    issueData.add(new Issue(
                            result.getInt("Book_Issue_ID"),
                            result.getInt("Book_ID"),
                            result.getInt("Member_ID"),
                            result.getString("Date_Issue"),
                            result.getString("Date_Return"),
                            result.getString("Book_Issue_Status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        IssueView.setItems(issueData);
    }

    @FXML
    private void addData() {
        try (Connection connection = DBConnection.getConnection()) {
            // Query to get the maximum ID
            String maxIdQuery = "SELECT MAX(Book_Issue_ID) AS max_id FROM Book_Issue";
            int issueID = 1;

            // Get the next issue ID
            try (PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery)) {
                ResultSet resultSet = maxIdStatement.executeQuery();
                if (resultSet.next()) {
                    int maxId = resultSet.getInt("max_id");
                    if (!resultSet.wasNull()) {
                        issueID = maxId + 1;
                    }
                }
            }

            // Insert query
            String query = "INSERT INTO Book_Issue (Book_Issue_ID, Book_ID, Member_ID, Date_Issue, Date_Return, Book_Issue_Status) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, issueID);

                // Get selected Book and set its ID
                Book selectedBook = bookIdCombo.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    statement.setInt(2, selectedBook.getBookID());
                } else {
                    System.err.println("No book selected");
                    return; // Exit if no book is selected
                }

                // Get selected Member and set its ID
                Member selectedMember = memberIdCombo.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    statement.setInt(3, selectedMember.getId()); // Use the getId() method
                } else {
                    System.err.println("No member selected");
                    return; // Exit if no member is selected
                }

                // Setting issue and return dates
                statement.setString(4, issueDatePicker.getValue() != null ? issueDatePicker.getValue().toString() : null);
                statement.setString(5, returnDatePicker.getValue() != null ? returnDatePicker.getValue().toString() : null);

                // Set the status
                statement.setString(6, statusCombo.getSelectionModel().getSelectedItem());

                // Execute the update
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error adding data: " + e.getMessage());
        }
    }

    @FXML
    private void updateData() {
        if (IssueView.getSelectionModel().getSelectedItem() != null) {
            try (Connection connection = DBConnection.getConnection()) {
                String query = "UPDATE Book_Issue SET Book_ID = ?, Member_ID = ?, Date_Issue = ?, Date_Return = ?, Book_Issue_Status = ? WHERE Book_Issue_ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    // Get selected Book and set its ID
                    Book selectedBook = bookIdCombo.getSelectionModel().getSelectedItem();
                    if (selectedBook != null) {
                        statement.setInt(1, selectedBook.getBookID());
                    } else {
                        System.err.println("No book selected");
                        return;
                    }

                    // Get selected Member and set its ID
                    Member selectedMember = memberIdCombo.getSelectionModel().getSelectedItem();
                    if (selectedMember != null) {
                        statement.setInt(2, selectedMember.getId());
                    } else {
                        System.err.println("No member selected");
                        return;
                    }

                    // Setting issue and return dates
                    statement.setString(3, issueDatePicker.getValue() != null ? issueDatePicker.getValue().toString() : null);
                    statement.setString(4, returnDatePicker.getValue() != null ? returnDatePicker.getValue().toString() : null);

                    // Set the status
                    statement.setString(5, statusCombo.getSelectionModel().getSelectedItem());


                    // Execute the update
                    statement.executeUpdate();
                }
                loadData();
            } catch (SQLException e) {
                System.err.println("Error updating data: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clearFields() {
        bookIdCombo.getSelectionModel().clearSelection();
        memberIdCombo.getSelectionModel().clearSelection();
        issueDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        statusCombo.getSelectionModel().clearSelection();
    }

    @FXML
    private void deleteData() {
        Issue selectedIssue = IssueView.getSelectionModel().getSelectedItem();
        if (selectedIssue != null) {
            try (Connection connection = DBConnection.getConnection()) {
                String query = "DELETE FROM Book_Issue WHERE Book_Issue_ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, selectedIssue.getIssueID());
                    statement.executeUpdate();
                }
                loadData();
            } catch (SQLException e) {
                System.err.println("Error deleting data: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exit() throws IOException {
        Main.changeScene();
    }


    private void loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Book_List"; // Replace 'Books' with your actual table name
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("Book_Id"); // Replace 'bookID' with your actual column name
                    String title = resultSet.getString("Book_Title"); // Replace 'bookTitle' with your actual column name
                    double price = resultSet.getDouble("Price"); // Adjust according to your schema
                    int bookNumber = resultSet.getInt("Book_Numbers"); // Adjust according to your schema
                    String ISBN10 = resultSet.getString("ISBN_10"); // Add the new column to your query
                    String ISBN13 = resultSet.getString("ISBN_13"); // Add the new column to your query
                    //books.add(new Book(id, title, price, bookNumber, ISBN10, ISBN13));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        bookIdCombo.setItems(books);
    }

    private void loadMembers() {
        ObservableList<Member> members = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT Member_ID, Member_Name FROM Members";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int memberId = resultSet.getInt("Member_ID");
                    String memberName = resultSet.getString("Member_Name");
                    members.add(new Member(memberId, memberName, null, null, null, null, null));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        memberIdCombo.setItems(members);
    }
}