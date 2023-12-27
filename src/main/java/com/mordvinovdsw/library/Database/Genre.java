package com.mordvinovdsw.library.Database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Genre {
    private SimpleIntegerProperty genreID;
    private SimpleStringProperty genreName;



    public Genre(int genreID, String genreName) {
        this.genreID = new SimpleIntegerProperty(genreID);
        this.genreName = new SimpleStringProperty(genreName);
    }

    public int getGenreID() {
        return genreID.get();
    }

    public String getGenreName() {
        return genreName.get();
    }

    @Override
    public String toString() {
        return genreName.get();
    }
}

