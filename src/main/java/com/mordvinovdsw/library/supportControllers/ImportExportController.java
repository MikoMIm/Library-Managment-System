package com.mordvinovdsw.library.supportControllers;

import com.mordvinovdsw.library.utils.ExportImportUtil;
import com.mordvinovdsw.library.utils.LoginWarning;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ImportExportController {
    public Button ImportButton;
    public Button ExportButton;
    public Button CreateDataButton;
    @FXML
    private AnchorPane rootAnchorPane;
    private final ExportImportUtil exportImportUtil;

    public ImportExportController() {
        exportImportUtil = new ExportImportUtil();
    }

    @FXML
    private void importDataBase() {
        System.out.println("Import button clicked");
        String destinationFilePath = "library.db";
        exportImportUtil.importDatabase(destinationFilePath);
        closeImportExportController();
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
        closeImportExportController();
    }

    private void closeImportExportController() {
        Stage currentStage = (Stage) rootAnchorPane.getScene().getWindow();
        currentStage.close();
        LoginWarning loginWarning = new LoginWarning();
        loginWarning.showLoginWarning();
    }
}