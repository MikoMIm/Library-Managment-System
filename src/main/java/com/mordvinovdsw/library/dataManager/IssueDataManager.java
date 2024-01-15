package com.mordvinovdsw.library.dataManager;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.Issue;

import java.sql.*;

public class IssueDataManager {
    public int insertIssue(int bookId, int memberId, String issueDate, String returnDate, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtIssue = null;
        PreparedStatement pstmtBook = null;
        String sqlInsertIssue = "INSERT INTO Book_Issue (Book_Id, Member_Id, Issue_Date, Return_Date, Status) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateBook = "UPDATE Book_List SET Book_Numbers = Book_Numbers - 1 WHERE Book_Id = ?";

        try {
            conn = DBConnection.getConnection();
            // Disable auto-commit for transactional integrity
            conn.setAutoCommit(false);

            // Inserting the new issue
            pstmtIssue = conn.prepareStatement(sqlInsertIssue, Statement.RETURN_GENERATED_KEYS);
            pstmtIssue.setInt(1, bookId);
            pstmtIssue.setInt(2, memberId);
            pstmtIssue.setString(3, issueDate);
            pstmtIssue.setString(4, returnDate);
            pstmtIssue.setString(5, status);

            int affectedRows = pstmtIssue.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating issue failed, no rows affected.");
            }

            // Updating the Book_Numbers in Book_List
            pstmtBook = conn.prepareStatement(sqlUpdateBook);
            pstmtBook.setInt(1, bookId);
            pstmtBook.executeUpdate();

            // Committing the transaction
            conn.commit();

            // Returning the newly created issue ID
            try (ResultSet generatedKeys = pstmtIssue.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating issue failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rolling back in case of error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (pstmtIssue != null) pstmtIssue.close();
            if (pstmtBook != null) pstmtBook.close();
            if (conn != null) {
                conn.setAutoCommit(true); // Resetting auto-commit
                conn.close();
            }
        }
    }
    public void updateIssue(int issueId, int bookId, int memberId, String issueDate, String returnDate, String status) throws SQLException {
        String sqlUpdateIssue = "UPDATE Book_Issue SET Book_Id = ?, Member_Id = ?, Issue_Date = ?, Return_Date = ?, Status = ? WHERE Issue_Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateIssue)) {

            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setString(3, issueDate);
            pstmt.setString(4, returnDate);
            pstmt.setString(5, status);
            pstmt.setInt(6, issueId);
            pstmt.executeUpdate();
        }
    }
    public void removeIssue(int issueId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtFetchBookId = null;
        PreparedStatement pstmtDeleteIssue = null;
        PreparedStatement pstmtUpdateBook = null;

        String fetchBookIdSql = "SELECT Book_Id FROM Book_Issue WHERE Issue_Id = ?";
        String deleteIssueSql = "DELETE FROM Book_Issue WHERE Issue_Id = ?";
        String updateBookSql = "UPDATE Book_List SET Book_Numbers = Book_Numbers + 1 WHERE Book_Id = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Fetching Book_Id for the issue
            pstmtFetchBookId = conn.prepareStatement(fetchBookIdSql);
            pstmtFetchBookId.setInt(1, issueId);
            int bookId = -1;
            ResultSet rs = pstmtFetchBookId.executeQuery();
            if (rs.next()) {
                bookId = rs.getInt("Book_Id");
            }
            rs.close();
            pstmtFetchBookId.close();

            if (bookId == -1) {
                throw new SQLException("Issue not found, cannot retrieve Book_Id.");
            }

            // Deleting the issue
            pstmtDeleteIssue = conn.prepareStatement(deleteIssueSql);
            pstmtDeleteIssue.setInt(1, issueId);
            pstmtDeleteIssue.executeUpdate();
            pstmtDeleteIssue.close();

            // Updating Book_List to increment Book_Numbers
            pstmtUpdateBook = conn.prepareStatement(updateBookSql);
            pstmtUpdateBook.setInt(1, bookId);
            pstmtUpdateBook.executeUpdate();
            pstmtUpdateBook.close();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback in case of error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Issue fetchIssueById(int issueId) throws SQLException {
        String sql = "SELECT * FROM Book_Issue WHERE Issue_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, issueId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("Book_Id");
                int memberId = rs.getInt("Member_Id");
                String issueDate = rs.getString("Issue_Date");
                String returnDate = rs.getString("Return_Date");
                String status = rs.getString("Status");


                return new Issue(issueId, bookId, memberId, issueDate, returnDate, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
}

