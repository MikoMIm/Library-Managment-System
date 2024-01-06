package com.mordvinovdsw.library.models;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Issue {
    private int issueId;
    private int bookId;
    private int memberId;
    private String issueDate;
    private String returnDate;
    private String status;

    // Constructor, getters, and setters
    public Issue(int issueId, int bookId, int memberId, String issueDate, String returnDate, String status) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getIssueId() {
        return issueId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
