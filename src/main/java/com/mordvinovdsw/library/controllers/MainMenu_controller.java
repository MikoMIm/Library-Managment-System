package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.Main;
import com.mordvinovdsw.library.utils.ScreenSizeConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
}
