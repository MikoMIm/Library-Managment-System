package com.mordvinovdsw.library.Database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Author {
    private SimpleIntegerProperty authorID;
    private SimpleStringProperty authorName;

    public Author(int authorID, String authorName) {
        this.authorID = new SimpleIntegerProperty(authorID);
        this.authorName = new SimpleStringProperty(authorName);
    }


    public int getAuthorID() {
        return authorID.get();
    }

    public void setAuthorID(int authorID) {
        this.authorID.set(authorID);
    }

    public String getAuthorName() {
        return authorName.get();
    }

    public void setAuthorName(String authorName) {
        this.authorName.set(authorName);
    }


    public IntegerProperty authorIDProperty() {
        return authorID;
    }

    public StringProperty authorNameProperty() {
        return authorName;
    }

    @Override
    public String toString() {
        return authorName.get();
    }
}

