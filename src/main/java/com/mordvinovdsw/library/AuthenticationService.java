package com.mordvinovdsw.library;
import com.mordvinovdsw.library.Database.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());
    public boolean validateCredentials(String username, String password) {
        String sql = "SELECT password FROM Users WHERE login = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return BCrypt.checkpw(password, storedHash);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in validateCredentials", e);
        }
        return false;
    }

    public boolean doesUserExist() {
        String sql = "SELECT COUNT(*) AS user_count FROM Users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() && rs.getInt("user_count") > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in validateCredentials", e);
        }
        return false;
    }
}


