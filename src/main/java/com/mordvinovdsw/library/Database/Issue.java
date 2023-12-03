package com.mordvinovdsw.library.Database;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Issue {
    private SimpleIntegerProperty issueID;
    private SimpleIntegerProperty bookID;
    private SimpleIntegerProperty memberID;
    private SimpleStringProperty dateIssue;
    private SimpleStringProperty dateReturn;
    private SimpleStringProperty status;

    // Constructor
    public Issue(int issueID, int bookID, int memberID, String dateIssue, String dateReturn, String status) {
        this.issueID = new SimpleIntegerProperty(issueID);
        this.bookID = new SimpleIntegerProperty(bookID);
        this.memberID = new SimpleIntegerProperty(memberID);
        this.dateIssue = new SimpleStringProperty(dateIssue);
        this.dateReturn = new SimpleStringProperty(dateReturn);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and setters
    public int getIssueID() {
        return issueID.get();
    }

    public void setIssueID(int issueID) {
        this.issueID.set(issueID);
    }

    public int getBookID() {
        return bookID.get();
    }

    public void setBookID(int bookID) {
        this.bookID.set(bookID);
    }

    public int getMemberID() {
        return memberID.get();
    }

    public void setMemberID(int memberID) {
        this.memberID.set(memberID);
    }

    public String getDateIssue() {
        return dateIssue.get();
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue.set(dateIssue);
    }

    public String getDateReturn() {
        return dateReturn.get();
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn.set(dateReturn);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    // Property accessors for JavaFX binding
    public SimpleIntegerProperty issueIDProperty() {
        return issueID;
    }

    public SimpleIntegerProperty bookIDProperty() {
        return bookID;
    }

    public SimpleIntegerProperty memberIDProperty() {
        return memberID;
    }

    public SimpleStringProperty dateIssueProperty() {
        return dateIssue;
    }

    public SimpleStringProperty dateReturnProperty() {
        return dateReturn;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}