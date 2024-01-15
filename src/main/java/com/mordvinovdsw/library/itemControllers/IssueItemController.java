package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.IssueDAO;
import com.mordvinovdsw.library.dataManager.IssueDataManager;
import com.mordvinovdsw.library.models.Issue;
import com.mordvinovdsw.library.supportControllers.EditIssueController;
import com.mordvinovdsw.library.utils.DataChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IssueItemController {
    private Runnable refreshCallback;
    private IssueDataManager issueDataManager = new IssueDataManager();
    private Issue issue;
    private static final Logger LOGGER = Logger.getLogger(IssueItemController.class.getName());
    public Button remove;
    public Button edit;

    @FXML
    private Label idLabel;
    @FXML
    private Label bookidLabel;
    @FXML
    private Label memberidLabel;
    @FXML
    private Label IssueDateLabel;
    @FXML
    private Label ReturnDateLabel;
    @FXML
    private Label StatusLabel;
    private DataChangeListener dataChangeListener;

    private IssueDAO issueDAO;

    public IssueItemController() {
        issueDAO = new IssueDAO();
    }
    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }
    private void notifyDataChangeListener() {
        if (dataChangeListener != null) {
            dataChangeListener.onDataChange();
        }
    }
    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
        idLabel.setText("Issue ID: " + issue.getIssueId());
        bookidLabel.setText("Book ID / Title: " + "[" + issue.getBookId() + " - " + getBookTitle(issue.getBookId()) + "]");
        memberidLabel.setText("Member ID / Name: " + "[" + issue.getMemberId() + " - " + getMemberName(issue.getMemberId()) + "]");
        IssueDateLabel.setText("Issue Date: " + issue.getIssueDate());
        ReturnDateLabel.setText("Return Date: " + issue.getReturnDate());
        Text statusText = new Text("Status: ");
        statusText.setFont(new Font("System", 18));
        statusText.setFill(Color.WHITE);

        Text statusValue = new Text(issue.getStatus());
        statusValue.setFont(new Font("System", 18));
        switch (issue.getStatus()) {
            case "Issued":
                statusValue.setFill(Color.GREEN);
                break;
            case "Returned":
                statusValue.setFill(Color.BLUE);
                break;
            case "Overdue":
                statusValue.setFill(Color.RED);
                break;
            default:
                statusValue.setFill(Color.BLACK);
                break;
        }

        TextFlow statusFlow = new TextFlow(statusText, statusValue);
        StatusLabel.setGraphic(statusFlow);
    }

    private String getBookTitle(int bookID) {
        String title = "Unknown";
        String sql = "SELECT Book_Title FROM Book_List WHERE Book_Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    title = rs.getString("Book_Title");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error fetching book title for Book ID: " + bookID, e);
        }
        return title;
    }

    private String getMemberName(int memberID) {
        String name = "Unknown";
        String sql = "SELECT Member_Name FROM Members WHERE Member_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("Member_Name");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error fetching member name for Member ID: " + memberID, e);
        }
        return name;
    }

    @FXML
    private void openEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Issue.fxml")); // Check this path
            Parent root = loader.load();
            EditIssueController editController = loader.getController();
            editController.prepareEdit(issue);
            editController.setRefreshCallback(this::refreshData);

            Stage editStage = new Stage();
            editStage.setTitle("Edit Issue");
            editStage.setScene(new Scene(root));
            editStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening edit window for Issue ID: " + issue.getIssueId(), e);
        }
    }

    @FXML
    private void removeData() {
        try {
            issueDataManager.removeIssue(issue.getIssueId());
            refreshData();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in removeData for Issue ID: " + issue.getIssueId(), e);
        }
    }

    private void refreshData() {
        if (this.issue == null) {
            LOGGER.log(Level.SEVERE, "Issue data is not set.");
            return;
        }

        try {
            Issue updatedIssue = issueDAO.fetchIssueById(issue.getIssueId());
            if (updatedIssue != null) {
                setIssue(updatedIssue);
            } else {
                LOGGER.log(Level.INFO, "No data found for Issue ID: " + issue.getIssueId());
            }
            notifyDataChangeListener();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in refreshData for Issue ID: " + issue.getIssueId(), e);
        }
    }

}

