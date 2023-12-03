package com.mordvinovdsw.library.controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.io.IOException;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.Issue;
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
    private TextField idField;
    @FXML
    private ComboBox<String> bookIdCombo;
    @FXML
    private ComboBox<String> memberIdCombo;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private ComboBox<String> statusCombo;

    private ObservableList<Issue> issueData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
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
            String maxIdQuery = "SELECT MAX(Book_Issue_ID) AS max_id FROM Book_Issue";
            int issueID = 1;

            try (PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery)) {
                ResultSet resultSet = maxIdStatement.executeQuery();
                if (resultSet.next()) {
                    int maxId = resultSet.getInt("max_id");
                    if (!resultSet.wasNull()) {
                        issueID = maxId + 1;
                    }
                }
            }
            String query = "INSERT INTO Book_Issue (Book_Issue_ID, Book_ID, Member_ID, Date_Issue, Date_Return, Book_Issue_Status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, issueID);
                statement.setInt(2, Integer.parseInt(bookIdCombo.getSelectionModel().getSelectedItem()));
                statement.setInt(3, Integer.parseInt(memberIdCombo.getSelectionModel().getSelectedItem()));
                statement.setString(4, issueDatePicker.getValue() != null ? issueDatePicker.getValue().toString() : null);
                statement.setString(5, returnDatePicker.getValue() != null ? returnDatePicker.getValue().toString() : null);
                statement.setString(6, statusCombo.getSelectionModel().getSelectedItem());
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
                    statement.setInt(1, Integer.parseInt(bookIdCombo.getSelectionModel().getSelectedItem()));
                    statement.setInt(2, Integer.parseInt(memberIdCombo.getSelectionModel().getSelectedItem()));
                    statement.setString(3, issueDatePicker.getValue().toString());
                    statement.setString(4, returnDatePicker.getValue().toString());
                    statement.setString(5, statusCombo.getSelectionModel().getSelectedItem());
                    statement.setInt(6, Integer.parseInt(idField.getText()));
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
        idField.clear();
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
}