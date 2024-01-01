package com.mordvinovdsw.library.models;

public class Author {
    final private int authorID;
    final private String authorName;

    public Author(int authorID, String authorName) {
        this.authorID = authorID;
        this.authorName = authorName;
    }

    public int getAuthorID() {
        return authorID;
    }

    @Override
    public String toString() {
        return authorName;
    }
}