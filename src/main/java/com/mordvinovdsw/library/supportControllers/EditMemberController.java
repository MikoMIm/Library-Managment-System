package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.dataManager.MemberDataManager;
import com.mordvinovdsw.library.models.Member;
import com.mordvinovdsw.library.utils.DataChangeListener;
import com.mordvinovdsw.library.utils.DialogUtil;
import com.mordvinovdsw.library.utils.StageUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditMemberController {

    @FXML
    private TextField memberNameField, memberNumberField, memberEmailField;
    @FXML
    private DatePicker memberRegisterComboBox, MemberExpiryComboBox;
    @FXML
    private ComboBox<String>  memberStatusComboBox;
    @FXML
    private Button addButton, saveButton, cancelButton;
    private Runnable refreshCallback;
    private int currentMemberId;
    private final MemberDataManager memberDataManager = new MemberDataManager();

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void initialize() {
        setupStatusComboBox();
    }
    private void finishDataUpdate() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }
    private void setupStatusComboBox() {
        memberStatusComboBox.setItems(FXCollections.observableArrayList("Active", "Inactive", "Suspended", "Expired"));
    }


    public void prepareEdit(Member member) {
        fillForm(member);
        currentMemberId = member.getId();
        saveButton.setVisible(true);
        addButton.setVisible(false);
    }

    public void prepareAdd() {
        memberNameField.clear();
        memberNumberField.clear();
        memberEmailField.clear();
        memberRegisterComboBox.setValue(null);
        MemberExpiryComboBox.setValue(null);
        memberStatusComboBox.setValue(null);
        saveButton.setVisible(false);
        addButton.setVisible(true);
    }

    private void fillForm(Member member) {
        memberNameField.setText(member.getName());
        memberNumberField.setText(member.getPhone());
        memberEmailField.setText(member.getEmail());
        memberRegisterComboBox.setValue(LocalDate.parse(member.getRegisterDate()));
        MemberExpiryComboBox.setValue(LocalDate.parse(member.getExpairDate()));
        memberStatusComboBox.setValue(member.getStatus());
    }


    @FXML
    private void addData() {
        if (validateInput()) {
            return;
        }
        try {
            memberDataManager.insertMember(
                    memberNameField.getText(),
                    memberNumberField.getText(),
                    memberEmailField.getText(),
                    memberRegisterComboBox.getValue().toString(),
                    MemberExpiryComboBox.getValue().toString(),
                    memberStatusComboBox.getValue()
            );
            DialogUtil.showDialog("Success", "Member added successfully.");
            finishDataUpdate();
            cancel();
        } catch (SQLException e) {
            DialogUtil.showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void saveData() {
        if (validateInput()) {
            return;
        }
        try {
            memberDataManager.updateMember(
                    currentMemberId,
                    memberNameField.getText(),
                    memberNumberField.getText(),
                    memberEmailField.getText(),
                    memberRegisterComboBox.getValue().toString(),
                    MemberExpiryComboBox.getValue().toString(),
                    memberStatusComboBox.getValue()
            );
            DialogUtil.showDialog("Success", "Member updated successfully.");
            if (refreshCallback != null) {
                refreshCallback.run();
            }
            cancel();
        } catch (SQLException e) {
            DialogUtil.showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        if (memberNameField.getText().trim().isEmpty() ||
                memberNumberField.getText().trim().isEmpty() ||
                memberEmailField.getText().trim().isEmpty() ||
                memberRegisterComboBox.getValue() == null ||
                MemberExpiryComboBox.getValue() == null ||
                memberStatusComboBox.getValue() == null || memberStatusComboBox.getValue().trim().isEmpty()) {
            DialogUtil.showError("All fields are required.");
            return true;
        }

        if (!memberEmailField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            DialogUtil.showError("Email format is invalid.");
            return true;
        }

        if (!memberNumberField.getText().matches("\\+?\\d+")) {
            DialogUtil.showError("Phone number must contain only digits.");
            return true;
        }

        return false;
    }

}