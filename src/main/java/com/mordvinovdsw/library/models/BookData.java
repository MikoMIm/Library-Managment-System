package com.mordvinovdsw.library.models;

import java.util.ArrayList;
import java.util.List;

public class BookData {
        private String title;
        private List<String> authors = new ArrayList<>();
        private List<String> genres = new ArrayList<>();
        private String isbn10;
        private String isbn13;


        public String getTitle() {
            return title;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public List<String> getGenres() {
            return genres;
        }

        public String getIsbn10() {
            return isbn10;
        }

        public String getIsbn13() {
            return isbn13;
        }


        public void setTitle(String title) {
            this.title = title;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public void setIsbn10(String isbn10) {
            this.isbn10 = isbn10;
        }

        public void setIsbn13(String isbn13) {
            this.isbn13 = isbn13;
        }

        @Override
        public String toString() {
            return "Title: " + title + ", Authors: " + authors + ", Genres: " + genres +
                    ", ISBN-10: " + isbn10 + ", ISBN-13: " + isbn13;
        }
    }

