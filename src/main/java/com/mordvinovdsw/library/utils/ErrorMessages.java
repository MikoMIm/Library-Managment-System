package com.mordvinovdsw.library.utils;

import javafx.scene.control.Alert;

public class ErrorMessages {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
