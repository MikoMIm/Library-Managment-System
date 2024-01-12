package com.mordvinovdsw.library.controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mordvinovdsw.library.itemControllers.IssueItemController;
import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.Issue;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.supportControllers.EditIssueController;
import com.mordvinovdsw.library.utils.ComboBoxUtil;
import com.mordvinovdsw.library.utils.DialogUtil;
import com.mordvinovdsw.library.utils.IssueStatusChecker;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Issue_Controller implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(LogIn_Controller.class.getName());
    @FXML private GridPane gridPane;
    @FXML private TextField searchTextField;
    @FXML private ComboBox<String> searchComboBox;
    @FXML private ComboBox<String> sortComboBox;

    private List<Issue> issues;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSortComboBox();
        issues = getIssuesFromDatabase();
        populateGridWithIssues(issues);
        ComboBoxUtil.fillIssueSearchOptions(searchComboBox);
        searchComboBox.setValue("Issue ID");
        IssueStatusChecker statusChecker = new IssueStatusChecker(issues, this);
        statusChecker.checkOverdueIssues();
        ComboBoxUtil.fillIssueSearchOptions(searchComboBox);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> handleIssueSearchAction(newValue));
        sortComboBox.setValue("Issue ID");
        sortComboBox.setOnAction(event -> sortData());
    }

    private void initializeSortComboBox() {
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Issue ID",
                "Book ID",
                "Member ID",
                "Issue Date",
                "Return Date",
                "Status"
        );
        sortComboBox.setItems(sortOptions);
        sortComboBox.setValue("Issue ID");
    }

    public List<Issue> getIssuesFromDatabase() {
        List<Issue> issues = FXCollections.observableArrayList();
        String sql = "SELECT Issue_Id, Book_Id, Member_Id, Issue_Date, Return_Date, Status FROM Book_Issue";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int issueId = rs.getInt("Issue_Id");
                int bookId = rs.getInt("Book_Id");
                int memberId = rs.getInt("Member_Id");
                String issueDate = rs.getString("Issue_Date");
                String returnDate = rs.getString("Return_Date");
                String status = rs.getString("Status");

                Issue issue = new Issue(issueId, bookId, memberId, issueDate, returnDate, status);
                issues.add(issue);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error when fetching issues from the database", e);
        }
        return issues;
    }

    public void populateGridWithIssues(List<Issue> issues) {
        gridPane.getChildren().clear();

        final int maxColumn = 3;
        int row = 0, column = 0;
        for (Issue issue : issues) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/layouts/issue_items.fxml"));
                VBox issueItemBox = loader.load();
                IssueItemController itemController = loader.getController();
                itemController.setIssue(issue);
                gridPane.add(issueItemBox, column, row);
                column++;
                if (column >= maxColumn) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IO Error when populating issues in the grid", e);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Issue.fxml"));
            Parent root = loader.load();
            EditIssueController editIssueController = loader.getController();
            editIssueController.prepareAdd();
            Stage stage = new Stage();
            stage.setTitle("Add New Issue");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            DialogUtil.showError("IO Error: " + e.getMessage());
        }
    }
    private void handleIssueSearchAction(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            populateGridWithIssues(issues);
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        String searchCriterion = searchComboBox.getValue();

        List<Issue> filteredIssues = issues.stream()
                .filter(issue -> {
                    switch (searchCriterion) {
                        case "Issue ID":
                            return String.valueOf(issue.getIssueId()).contains(lowerCaseSearchText);
                        case "Book ID":
                            return String.valueOf(issue.getBookId()).contains(lowerCaseSearchText);
                        case "Member ID":
                            return String.valueOf(issue.getMemberId()).contains(lowerCaseSearchText);
                        case "Issue Date":
                            return issue.getIssueDate().contains(lowerCaseSearchText);
                        case "Return Date":
                            return issue.getReturnDate() != null && issue.getReturnDate().contains(lowerCaseSearchText);
                        case "Status":
                            return issue.getStatus().toLowerCase().contains(lowerCaseSearchText);
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

        populateGridWithIssues(filteredIssues);
    }

    @FXML
    private void sortData() {
        String selectedOption = sortComboBox.getValue();
        switch (selectedOption) {
            case "Issue ID":
                issues.sort(Comparator.comparingInt(Issue::getIssueId));
                break;
            case "Book ID":
                issues.sort(Comparator.comparingInt(Issue::getBookId));
                break;
            case "Member ID":
                issues.sort(Comparator.comparingInt(Issue::getMemberId));
                break;
            case "Issue Date":
                issues.sort(Comparator.comparing(issue -> LocalDate.parse(issue.getIssueDate())));
                break;
            case "Return Date":
                issues.sort(Comparator.nullsLast(Comparator.comparing(
                        issue -> issue.getReturnDate() == null ? null : LocalDate.parse(issue.getReturnDate())
                )));
                break;
            case "Status":
                issues.sort(Comparator.comparing(Issue::getStatus));
                break;
        }
        populateGridWithIssues(issues);
    }

    @FXML
    private void refreshGrid() {
        issues = getIssuesFromDatabase();
        populateGridWithIssues(issues);
        LOGGER.log(Level.INFO, "Grid has been refreshed with updated issue data.");
    }
}