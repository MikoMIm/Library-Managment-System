package com.mordvinovdsw.library.itemControllers;

import com.mordvinovdsw.library.Database.DBConnection;
import com.mordvinovdsw.library.Database.MemberDAO;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.dataManager.MemberDataManager;
import com.mordvinovdsw.library.models.Member;
import com.mordvinovdsw.library.supportControllers.EditMemberController;
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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberItemController {
    private Runnable refreshCallback;
    private MemberDataManager memberDataManager = new MemberDataManager();
    private static final Logger LOGGER = Logger.getLogger(MemberItemController.class.getName());
    public Button remove;
    public Button edit;
    private Member member;

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label expiryLabel;
    @FXML
    private Label statusLabel;
    private MemberDAO memberDAO;

    public MemberItemController() {
        memberDAO = new MemberDAO();
    }

    public void setMember(Member member) {
        this.member = member;
        idLabel.setText("Member ID: " + member.getId());
        nameLabel.setText("Member Name: " + member.getName());
        phoneLabel.setText("Phone: " + member.getPhone());
        emailLabel.setText("Email: " + member.getEmail());
        dateLabel.setText("Date of Registration: " + member.getRegisterDate());
        expiryLabel.setText("Date of Expiry: " + member.getExpairDate());

        Text statusText = new Text("Status: ");
        statusText.setFont(new Font("System", 18));
        statusText.setFill(Color.WHITE);
        Text statusValue = new Text(member.getStatus());
        switch (member.getStatus()) {
            case "Active":
                statusValue.setFont(new Font("System", 18));
                statusValue.setFill(Color.GREEN);
                break;
            case "Inactive":
                statusValue.setFont(new Font("System", 18));
                statusValue.setFill(Color.YELLOW);
                break;
            case "Suspended":
                statusValue.setFont(new Font("System", 18));
                statusValue.setFill(Color.BLUE);
                break;
            case "Expired":
                statusValue.setFont(new Font("System", 18));
                statusValue.setFill(Color.RED);
                break;
            default:
                statusValue.setFont(new Font("System", 18));
                statusValue.setFill(Color.BLACK);
                break;
        }
        TextFlow statusFlow = new TextFlow(statusText, statusValue);
        statusLabel.setGraphic(statusFlow);
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }
    @FXML
    private void openEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Member.fxml"));
            Parent root = loader.load();
            EditMemberController editController = loader.getController();
            editController.prepareEdit(member);
            editController.setRefreshCallback(this::refreshData);
            Stage editStage = Main.createStage("Edit Member", root);
            editStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening edit window for Member ID: " + member.getId(), e);
        }
    }

    @FXML
    private void removeData() {
        String deleteMemberSql = "DELETE FROM Members WHERE Member_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtDeleteMember = conn.prepareStatement(deleteMemberSql)) {

            pstmtDeleteMember.setInt(1, member.getId());
            pstmtDeleteMember.executeUpdate();
            finishDataUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in removeData for Member ID: " + member.getId(), e);
        }
    }


    private void refreshData() {
        Member updatedMember = memberDAO.fetchMemberById(member.getId());
        if (updatedMember != null) {
            setMember(updatedMember);
        }
        finishDataUpdate();
    }

    private void finishDataUpdate() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }
}