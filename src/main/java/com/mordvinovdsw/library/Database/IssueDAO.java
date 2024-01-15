package com.mordvinovdsw.library.Database;

import com.mordvinovdsw.library.models.Issue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueDAO {
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
