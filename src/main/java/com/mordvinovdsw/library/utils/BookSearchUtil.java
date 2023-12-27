package com.mordvinovdsw.library.utils;

import com.mordvinovdsw.library.Database.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookSearchUtil {
    public static List<Book> searchBooks(List<Book> books, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return books;
        }
        String lowerCaseSearchText = searchText.toLowerCase();

        return books.stream()
                .filter(book -> book.getBookTitle().toLowerCase().contains(lowerCaseSearchText) ||
                        book.getISBN10().toLowerCase().contains(lowerCaseSearchText) ||
                        book.getISBN13().toLowerCase().contains(lowerCaseSearchText) ||
                        book.getGenre().toLowerCase().contains(lowerCaseSearchText))
                .collect(Collectors.toList());
    }
}
