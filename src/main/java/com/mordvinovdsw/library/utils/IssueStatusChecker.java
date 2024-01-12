package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.controllers.Issue_Controller;
import com.mordvinovdsw.library.models.Issue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IssueStatusChecker {
    private static final Logger LOGGER = Logger.getLogger(IssueStatusChecker.class.getName());
    private final List<Issue> issues;
    private final Issue_Controller controller;

    public IssueStatusChecker(List<Issue> issues, Issue_Controller controller) {
        this.issues = issues;
        this.controller = controller;
    }

    public void checkOverdueIssues() {
        for (Issue issue : issues) {
            LocalDate returnDate = LocalDate.parse(issue.getReturnDate());
            if (returnDate.isBefore(LocalDate.now()) && !issue.getStatus().equals("Overdue")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Overdue Issue Confirmation");
                alert.setHeaderText("Issue with ID " + issue.getIssueId() + " is overdue.");
                alert.setContentText("Do you want to change its status to \"Overdue\"?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    updateIssueStatusToOverdue(issue);
                    controller.populateGridWithIssues(controller.getIssuesFromDatabase());
                }
            }
        }
    }

    private void updateIssueStatusToOverdue(Issue issue) {
        String updateStatusSql = "UPDATE Book_Issue SET Status = 'Overdue' WHERE Issue_Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtUpdateStatus = conn.prepareStatement(updateStatusSql)) {

            pstmtUpdateStatus.setInt(1, issue.getIssueId());
            pstmtUpdateStatus.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update Issue Status", e);
        }
    }
}

