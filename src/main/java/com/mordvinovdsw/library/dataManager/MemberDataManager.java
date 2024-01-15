package com.mordvinovdsw.library.dataManager;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.models.Member;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberDataManager {
    private static final Logger LOGGER = Logger.getLogger(MemberDataManager.class.getName());
    public void insertMember(String name, String phoneNumber, String email, String registerDate, String expairDate, String status) throws SQLException {
        String sqlInsertMember = "INSERT INTO Members (Member_Name, Phone_Number, Email_adress, Register_Date, Date_Expair, Member_Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertMember, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, registerDate);
            pstmt.setString(5, expairDate);
            pstmt.setString(6, status);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
        }
    }

    public void updateMember(int memberId, String name, String phone, String email, String registerDate, String expairDate, String status) throws SQLException {
        {
            String sqlUpdateMember = "UPDATE Members SET Member_Name = ?, Phone_Number = ?, Email_adress = ?, Register_Date = ?, Date_Expair = ?, Member_Status = ? WHERE Member_ID = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sqlUpdateMember)) {

                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setString(3, email);
                pstmt.setString(4, registerDate);
                pstmt.setString(5, expairDate);
                pstmt.setString(6, status);
                pstmt.setInt(7, memberId);
                pstmt.executeUpdate();
            }
        }
    }

    public Member fetchMemberById(int memberId) {
        String sql = "SELECT * FROM Members WHERE Member_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("Member_ID");
                    String name = rs.getString("Member_Name");
                    String phone = rs.getString("Phone_Number");
                    String email = rs.getString("Email_adress");
                    String registerDate = rs.getString("Register_Date");
                    String expairDate = rs.getString("Date_Expair");
                    String status = rs.getString("Member_Status");

                    return new Member(id, name, phone, email, registerDate, expairDate, status);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in fetchMemberById for Member ID: " + memberId, e);
        }
        return null;
    }
}
