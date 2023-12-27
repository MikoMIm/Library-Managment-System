package com.mordvinovdsw.library.utils;

import javafx.scene.control.TextField;

public class TextFieldLimitUtil {
    public static void limitTextFieldLength(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(newValue.substring(0, maxLength));
            }
        });
    }
}
