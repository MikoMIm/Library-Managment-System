package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainMenu_controller {


    public void openBookList(ActionEvent event) throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Books_list.fxml", ScreenSizeConstants.MainControllerSize);
    }

    public void openIssueList(ActionEvent event) throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Issue_list.fxml",ScreenSizeConstants.MainControllerSize);
    }

    public void logout(ActionEvent event) throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/hello-view.fxml",ScreenSizeConstants.MainControllerSize);
    }

    public void openMemberList(ActionEvent event) throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Member_list.fxml",ScreenSizeConstants.MainControllerSize);
    }

    public void openAdminSettings(ActionEvent event) throws IOException {
        Main.getSceneController().setScene("/com/mordvinovdsw/library/Users_list.fxml",ScreenSizeConstants.MainControllerSize);
    }

    public void openImportExport(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mordvinovdsw/library/support_layouts/ImportExport_layout.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Import/Export");
        stage.setScene(new Scene(root));
        stage.show();
    }


}
