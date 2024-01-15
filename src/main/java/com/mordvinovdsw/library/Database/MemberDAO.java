package com.mordvinovdsw.library.Database;

import com.mordvinovdsw.library.itemControllers.MemberItemController;
import com.mordvinovdsw.library.models.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberDAO {
    private static final Logger LOGGER = Logger.getLogger(MemberDAO.class.getName());
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
