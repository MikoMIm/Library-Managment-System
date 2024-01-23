package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainMenu_controller {


    public GridPane mainMenuGrid;
    public Button memberListButton;
    public Button issueListButton;

    public void openBookList() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Books_list.fxml", ScreenSizeConstants.MainControllerSize);
        Main.getSceneController().setStageTitle("Book List");
    }

    public void openIssueList() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Issue_list.fxml",ScreenSizeConstants.MainControllerSize);
        Main.getSceneController().setStageTitle("Issue List");
    }

    public void logout() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/hello-view.fxml",ScreenSizeConstants.MainControllerSize);
        Main.getSceneController().setStageTitle("LogIn");
    }

    public void openMemberList() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Member_list.fxml",ScreenSizeConstants.MainControllerSize);
        Main.getSceneController().setStageTitle("Member List");
    }

    public void openAdminSettings() throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Users_list.fxml",ScreenSizeConstants.MainControllerSize);
        Main.getSceneController().setStageTitle("Admin Settings");
    }

    public void openImportExport() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/ImportExport_layout.fxml"));
        Parent root = loader.load();
        Stage stage = Main.createStage("Import/Export", root);
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.DECORATED);

        stage.show();
    }


}
