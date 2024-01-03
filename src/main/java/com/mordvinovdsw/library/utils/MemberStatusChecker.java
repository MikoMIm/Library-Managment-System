package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.controllers.Member_List_Controller;
import com.mordvinovdsw.library.models.Member;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MemberStatusChecker {
    private List<Member> members;
    private Member_List_Controller controller;

    public MemberStatusChecker(List<Member> members, Member_List_Controller controller) {
        this.members = members;
        this.controller = controller;
    }

    public void checkOverdueMembers() {
        for (Member member : members) {
            LocalDate expiryDate = LocalDate.parse(member.getExpairDate());
            if (expiryDate.isBefore(LocalDate.now()) && !member.getStatus().equals("Expired")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Member with index " + member.getId() + " with name " + member.getName() + " has overdue its library ticket.");
                alert.setContentText("Do you want to change its status to \"Expired\"?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    updateMemberStatusToExpired(member);
                    controller.populateGridWithMembers(controller.getMembersFromDatabase());
                }
            }
        }
    }

    private void updateMemberStatusToExpired(Member member) {
        String updateStatusSql = "UPDATE Members SET Member_Status = 'Expired' WHERE Member_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtUpdateStatus = conn.prepareStatement(updateStatusSql)) {

            pstmtUpdateStatus.setInt(1, member.getId());
            pstmtUpdateStatus.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
