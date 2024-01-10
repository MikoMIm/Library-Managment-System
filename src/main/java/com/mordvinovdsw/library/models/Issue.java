package com.mordvinovdsw.library.models;


public class Issue {
    private final int issueId;
    private final int bookId;
    private final int memberId;
    private final String issueDate;
    private final String returnDate;
    private String status;

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

    public void setStatus(String status) {
        this.status = status;
    }
}
