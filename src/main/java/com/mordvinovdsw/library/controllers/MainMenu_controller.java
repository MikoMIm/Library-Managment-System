package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenu_controller {


    public void pressed_bookList_button(ActionEvent event) throws IOException {
        Main.BookList();
    }

    public void pressed_Issue_button(ActionEvent event) throws IOException {
       try{
        Main.IssueList();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void logout(ActionEvent event) throws IOException {
        Main.Log_out();
    }

    public void pressed_memberList_button(ActionEvent event) throws IOException {
    try{
    Main.MemberList();
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

}
