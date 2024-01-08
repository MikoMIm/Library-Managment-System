package com.mordvinovdsw.library.controllers;

import com.mordvinovdsw.library.itemControllers.MemberItemController;
import com.mordvinovdsw.library.models.Member;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import com.mordvinovdsw.library.Database.DBConnection;

import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.supportControllers.EditBookController;
import com.mordvinovdsw.library.supportControllers.EditMemberController;
import com.mordvinovdsw.library.utils.ComboBoxUtil;
import com.mordvinovdsw.library.utils.ErrorMessages;
import com.mordvinovdsw.library.utils.MemberStatusChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Member_List_Controller implements Initializable {

    public AnchorPane rootAnchorPane;
    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchComboBox;
    @FXML
    private ComboBox<String> sortComboBox;
    private List<Member> members;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboBoxUtil.fillMemberSearchOptions(searchComboBox);
        members = getMembersFromDatabase();
        populateGridWithMembers(members);
        sortData();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction(newValue));
        searchComboBox.setOnAction(event -> handleSearchAction(searchTextField.getText()));
        MemberStatusChecker statusChecker = new MemberStatusChecker(members, this);
        statusChecker.checkOverdueMembers();

    }

    public List<Member> getMembersFromDatabase() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT Member_ID, Member_Name, Phone_Number, Email_adress, Register_Date, Date_Expair, Member_Status FROM Members";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Member_ID");
                String name = rs.getString("Member_Name");
                String phone = rs.getString("Phone_Number");
                String email = rs.getString("Email_adress");
                String registerDate = rs.getString("Register_Date");
                String expairDate = rs.getString("Date_Expair");
                String status = rs.getString("Member_Status");

                Member member = new Member(id, name, phone, email, registerDate, expairDate, status);
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public void populateGridWithMembers(List<Member> members) {
        gridPane.getChildren().clear();

        final int maxColumn = 3;
        int row = 0, column = 0;
        for (Member member : members) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/layouts/member_items.fxml"));
                VBox memberItemPane = loader.load();
                MemberItemController itemController = loader.getController();
                itemController.setMember(member);

                gridPane.add(memberItemPane, column, row);
                column++;
                if (column == maxColumn) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                ErrorMessages.showError("IO Error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exit() throws IOException {
        Main.changeScene();
    }

    @FXML
    private void addNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/Edit_Add_Member.fxml"));
            Parent root = loader.load();
            EditMemberController editController = loader.getController();
            editController.prepareAdd();
            Stage stage = new Stage();
            stage.setTitle("Add New Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            ErrorMessages.showError("IO Error: " + e.getMessage());
        }
    }

    @FXML
    private void sortData() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Member ID", "Member Name", "Member Phone Number", "Member Email",
                "Registration Date", "Date Expire", "Status"
        );
        sortComboBox.setItems(options);

        sortComboBox.setOnAction(event -> {
            String selectedOption = sortComboBox.getValue();
            switch (selectedOption) {
                case "Member ID":
                    members.sort(Comparator.comparingInt(Member::getId));
                    break;
                case "Member Name":
                    members.sort(Comparator.comparing(Member::getName));
                    break;
                case "Member Phone Number":
                    members.sort(Comparator.comparing(Member::getPhone));
                    break;
                case "Member Email":
                    members.sort(Comparator.comparing(Member::getEmail));
                    break;
                case "Registration Date":
                    members.sort(Comparator.comparing(Member::getRegisterDate));
                    break;
                case "Date Expire":
                    members.sort(Comparator.comparing(Member::getExpairDate));
                    break;
                case "Status":
                    members.sort(Comparator.comparing(Member::getStatus));
                    break;
            }
            populateGridWithMembers(members);
        });
    }

    @FXML
    private void handleSearchAction(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            populateGridWithMembers(members);
            return;
        }

        String lowerCaseSearchText = searchText.toLowerCase();
        String searchCriterion = searchComboBox.getValue();

        List<Member> filteredMembers = members.stream()
                .filter(member -> {
                    if (searchCriterion == null || searchCriterion.isEmpty()) {
                        return true;
                    }
                    return switch (searchCriterion) {
                        case "Member ID" -> String.valueOf(member.getId()).contains(lowerCaseSearchText);
                        case "Member Name" -> member.getName().toLowerCase().contains(lowerCaseSearchText);
                        case "Member Phone Number" -> member.getPhone().toLowerCase().contains(lowerCaseSearchText);
                        case "Member Email" -> member.getEmail().toLowerCase().contains(lowerCaseSearchText);
                        case "Registration Date" -> member.getRegisterDate().contains(lowerCaseSearchText);
                        case "Date Expire" -> member.getExpairDate().contains(lowerCaseSearchText);
                        case "Status" -> member.getStatus().toLowerCase().contains(lowerCaseSearchText);
                        default -> true;
                    };
                })
                .collect(Collectors.toList());

        populateGridWithMembers(filteredMembers);
    }
}
