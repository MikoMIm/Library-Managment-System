package com.mordvinovdsw.library.controllers;
import com.mordvinovdsw.library.LogIn;
import com.mordvinovdsw.library.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LogIn_Controller {

    private LogIn login;

    public LogIn_Controller(){
        login = new LogIn();
    }
    @FXML
    private Button button;

    @FXML
    private Label wrongLogin;

    @FXML
    private TextField userID;

    @FXML
    private PasswordField password;

    @FXML
    public void userLogin(ActionEvent event) throws IOException {
            checkLogin();
    }

    private void checkLogin() throws IOException {
        if(login.checkLogin(userID.getText(), password.getText())){
            wrongLogin.setText("Success!");
            Main.changeScene();
        }
        else if (userID.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter your data. ");
        }
        else{
            wrongLogin.setText("Wrong Login or password. ");
        }
    }





}

