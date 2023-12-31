package com.mordvinovdsw.library.dataManager;

import com.mordvinovdsw.library.models.Author;
import com.mordvinovdsw.library.models.Genre;

import java.util.Optional;

public class DataEntryManager {
    public Optional<Author> handleNewAuthorEntry(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return Optional.empty();
        }
        Author existingAuthor = AuthorUtil.findAuthorByName(authorName.trim());
        if (existingAuthor == null) {
            return Optional.ofNullable(AuthorUtil.addNewAuthorToDatabase(authorName.trim()));
        } else {
            return Optional.of(existingAuthor);
        }
    }

    public Optional<Genre> handleNewGenreEntry(String genreName) {
        if (genreName == null || genreName.trim().isEmpty()) {
            return Optional.empty();
        }
        Genre existingGenre = GenreUtil.findGenreByName(genreName.trim());
        if (existingGenre == null) {
            Genre newGenre = GenreUtil.addNewGenreToDatabase(genreName.trim());
            return Optional.ofNullable(newGenre);
        } else {
            return Optional.of(existingGenre);
        }
    }
}

