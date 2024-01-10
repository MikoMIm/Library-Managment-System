package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.dataManager.IssueDataManager;
import com.mordvinovdsw.library.models.IdentifiableItem;
import com.mordvinovdsw.library.models.Issue;
import com.mordvinovdsw.library.utils.ComboBoxUtil;
import com.mordvinovdsw.library.utils.DialogUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EditIssueController {
    @FXML
    private Button saveButton;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private ComboBox<IdentifiableItem> memberComboBox;
    @FXML
    private ComboBox<IdentifiableItem> bookComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    private IssueDataManager issueDataManager = new IssueDataManager();
    private boolean isEditMode = false;
    private Issue issue;


    @FXML
    private void initialize() {
        ComboBoxUtil.fillMemberComboBox(memberComboBox);
        ComboBoxUtil.fillBookComboBox(bookComboBox);
        fillStatusComboBox();

        setupComboBoxCellFactory(memberComboBox);
        setupComboBoxCellFactory(bookComboBox);
    }
    private void setupComboBoxCellFactory(ComboBox<IdentifiableItem> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<IdentifiableItem>() {
            @Override
            protected void updateItem(IdentifiableItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toString());
            }
        });

        comboBox.setConverter(new StringConverter<IdentifiableItem>() {
            @Override
            public String toString(IdentifiableItem object) {
                return object == null ? null : String.valueOf(object.getId());
            }

            @Override
            public IdentifiableItem fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(item -> item.getId() == Integer.parseInt(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    private void fillStatusComboBox() {
        List<String> statuses = Arrays.asList("Issued", "Returned", "Overdue");
        statusComboBox.setItems(FXCollections.observableArrayList(statuses));
        statusComboBox.setValue("Issued");
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
        if (issue != null) {
            issueDatePicker.setValue(LocalDate.parse(issue.getIssueDate()));
            returnDatePicker.setValue((issue.getReturnDate() != null) ? LocalDate.parse(issue.getReturnDate()) : null);
            statusComboBox.setValue(issue.getStatus());
            memberComboBox.getSelectionModel().select(findIdentifiableItemById(memberComboBox, issue.getMemberId()));
            bookComboBox.getSelectionModel().select(findIdentifiableItemById(bookComboBox, issue.getBookId()));
        }
    }

    private IdentifiableItem findIdentifiableItemById(ComboBox<IdentifiableItem> comboBox, int id) {
        for (IdentifiableItem item : comboBox.getItems()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @FXML
    private void saveData() {
        if (!validateInput()) {
            return;
        }
        try {
            int bookId = bookComboBox.getValue().getId();
            int memberId = memberComboBox.getValue().getId();
            String issueDate = (issueDatePicker.getValue() != null) ? issueDatePicker.getValue().toString() : "";
            String returnDate = (returnDatePicker.getValue() != null) ? returnDatePicker.getValue().toString() : null;
            String status = statusComboBox.getValue();

            if (isEditMode) {
                if (issue != null && issue.getIssueId() > 0) {
                    issueDataManager.updateIssue(issue.getIssueId(), bookId, memberId, issueDate, returnDate, status);
                } else {
                    DialogUtil.showError("Error: Invalid issue data for update.");
                    return;
                }
            } else {
                issueDataManager.insertIssue(bookId, memberId, issueDate, returnDate, status);
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

    @FXML
    private void addData() {
        if (!validateInput()) {
            return;
        }

        try {
            int bookId = bookComboBox.getValue().getId();
            int memberId = memberComboBox.getValue().getId();
            String issueDate = (issueDatePicker.getValue() != null) ? issueDatePicker.getValue().toString() : "";
            String returnDate = (returnDatePicker.getValue() != null) ? returnDatePicker.getValue().toString() : null;
            String status = statusComboBox.getValue();
            int issueId = issueDataManager.insertIssue(bookId, memberId, issueDate, returnDate, status);
            clearForm();
        } catch (SQLException e) {
            DialogUtil.showError("Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        memberComboBox.getSelectionModel().clearSelection();
        bookComboBox.getSelectionModel().clearSelection();
        issueDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        statusComboBox.getSelectionModel().clearSelection();
    }


    private boolean validateInput() {
            if (memberComboBox.getSelectionModel().getSelectedItem() == null) {
                DialogUtil.showError("Please select a member.");
                return false;
            }
            if (bookComboBox.getSelectionModel().getSelectedItem() == null) {
                DialogUtil.showError("Please select a book.");
                return false;
            }
            if (issueDatePicker.getValue() == null) {
                DialogUtil.showError("Please enter an issue date.");
                return false;
            } else if (issueDatePicker.getValue().isAfter(LocalDate.now())) {
                DialogUtil.showError("Issue date cannot be in the future.");
                return false;
            }
            if (returnDatePicker.getValue() != null && returnDatePicker.getValue().isBefore(issueDatePicker.getValue())) {
                DialogUtil.showError("Return date cannot be before the issue date.");
                return false;
            }
            if (statusComboBox.getValue() == null || statusComboBox.getValue().trim().isEmpty()) {
                DialogUtil.showError("Please select a status.");
                return false;
            }
            return true;
        }



    public void prepareEdit(Issue issue) {
        this.isEditMode = true;
        setIssue(issue);
        saveButton.setVisible(true);
        addButton.setVisible(false);
    }

    public void prepareAdd() {
        issueDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        memberComboBox.setValue(null);
        bookComboBox.setValue(null);
        statusComboBox.setValue(null);
        saveButton.setVisible(false);
        addButton.setVisible(true);
    }
}