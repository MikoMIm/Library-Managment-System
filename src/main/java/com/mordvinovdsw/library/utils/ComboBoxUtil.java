package com.mordvinovdsw.library.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;

public class ComboBoxUtil {
    public static void fillBookSearchOptions(ComboBox<String> comboBox) {
        comboBox.setItems(FXCollections.observableArrayList(
                "Book ID", "Book Name", "Book Genre", "Book ISBN"
        ));
    }

    public static <T> void fillComboBoxWithObjects(ComboBox<T> comboBox, List<T> items) {
        ObservableList<T> options = FXCollections.observableArrayList(items);
        comboBox.setItems(options);
    }
}
