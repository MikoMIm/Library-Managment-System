package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.utils.DialogUtil;
import com.mordvinovdsw.library.utils.ExportImportUtil;
import com.mordvinovdsw.library.utils.LoginScreenUtil;
import javafx.fxml.FXML;


public class ImportExportController {


    private final ExportImportUtil exportImportUtil;

    public ImportExportController() {
        exportImportUtil = new ExportImportUtil();
    }

    @FXML
    private void importDataBase() {
        System.out.println("Import button clicked");
        String destinationFilePath = "library.db"; // Path to the destination database
        exportImportUtil.importDatabase(destinationFilePath);
        restartApplicationWithConfirmation();
    }

    @FXML
    private void exportDataBase() {
        System.out.println("Export button clicked");
        String sourceFilePath = "library.db";
        exportImportUtil.exportDatabase(sourceFilePath);
    }

    @FXML
    private void createDataBase() {
        String databasePath = "library.db";
        exportImportUtil.createNewDatabase(databasePath);
        restartApplicationWithConfirmation();
    }

    private void restartApplicationWithConfirmation() {
        boolean confirm = DialogUtil.showConfirmationDialog("Restart Required",
                "The application needs to restart to apply changes. Restart now?");
        if (confirm) {
            LoginScreenUtil.openLoginScreen();
        }
    }
}