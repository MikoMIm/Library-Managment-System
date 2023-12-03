package com.mordvinovdsw.library.Database;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Book {
    private SimpleIntegerProperty bookID;
    private SimpleStringProperty bookTitle;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty bookNumber;

    public Book(int bookID, String bookTitle, double price, int bookNumber) {
        this.bookID = new SimpleIntegerProperty(bookID);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.price = new SimpleDoubleProperty(price);
        this.bookNumber = new SimpleIntegerProperty(bookNumber);
    }

    public int getBookID() {
        return bookID.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getBookNumber() {
        return bookNumber.get();
    }

    
}



