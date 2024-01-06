package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.dataManager.IssueDataManager;
import com.mordvinovdsw.library.models.Issue;
import com.mordvinovdsw.library.supportControllers.EditIssueController;
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

public class IssueItemController {
    private Issue issue;

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return name;
    }

    @FXML
    private void openEdit() {
        try {
            Stage editStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Issue.fxml"));
            Parent root = loader.load();
            EditIssueController editController = loader.getController();
            editController.prepareEdit(issue);
            editStage.setTitle("Edit Issue");
            editStage.setScene(new Scene(root));
            editStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeData() {
        try {
            IssueDataManager issueDataManager = new IssueDataManager();
            issueDataManager.removeIssue(issue.getIssueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

