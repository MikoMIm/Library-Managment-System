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

    public static void fillMemberSearchOptions(ComboBox<String> comboBox) {
        comboBox.setItems(FXCollections.observableArrayList(
                "Member ID", "Member Name", "Member Phone Number", "Member Email",
                "Registration Date", "Date Expire", "Status"
        ));
    }
}
