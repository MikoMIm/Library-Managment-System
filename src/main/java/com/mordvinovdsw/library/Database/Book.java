package com.mordvinovdsw.library.Database;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.List;

public class Book {
    private SimpleIntegerProperty bookID;
    private SimpleStringProperty bookTitle;
    private SimpleIntegerProperty bookNumber;
    private SimpleStringProperty ISBN10;
    private SimpleStringProperty ISBN13;
    private SimpleStringProperty genre;

    public Book(int bookID, String bookTitle, int bookNumber, String ISBN10, String ISBN13, String genre) {
        this.bookID = new SimpleIntegerProperty(bookID);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookNumber = new SimpleIntegerProperty(bookNumber);
        this.ISBN10 = new SimpleStringProperty(ISBN10);
        this.ISBN13 = new SimpleStringProperty(ISBN13);
        this.genre = new SimpleStringProperty(genre);
    }
    public String getGenre() {
        return genre.get();
    }
    public int getBookID() {
        return bookID.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public int getBookNumber() {
        return bookNumber.get();
    }

    public String getISBN10() {return ISBN10.get();}

    public String getISBN13() {return ISBN13.get();}


    @Override
    public String toString() {
        return bookID.get() + " - " + bookTitle.get();
    }
}




