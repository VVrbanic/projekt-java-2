package com.example.projektjava.controller;

import com.example.projektjava.HelloApplication;
import com.example.projektjava.AlertScreen;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.exceptions.ErrorWhileReadingFileException;
import com.example.projektjava.model.Printer;
import com.example.projektjava.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginController implements AlertScreen {
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;
    @FXML
    public Button login;
    @FXML
    public Button goBack;

    Printer<String> loginSuccess =new Printer("Login Successful");
    Printer<String> noMatch =new Printer("User matched incorrectly");
    Printer<String> faild =new Printer("Login Failed");
    Printer<String> wrongMailOrPass =new Printer("Krivi mail ili lozinka");

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @FXML
    protected void logInButtonClicked() throws IOException, ErrorWhileReadingFileException {
        List<String> messages = isFull();
        if (messages.isEmpty()) {
            if(ReadTextFileController.checkIfUserExists(email.getText(), password.getText())) {
                logger.info(loginSuccess.getPrintThing());
                Optional<User>  user = DataBase.getUserByEmailLogin(email.getText());
                if(user.isPresent()) {
                    UserSession.init(user.get());
                }else{
                    throw new DatabaseException(noMatch.getPrintThing());
                }
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-about.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                HelloApplication.getMainStage().setTitle("Welcome");
                HelloApplication.getMainStage().setScene(scene);
                HelloApplication.getMainStage().show();
            }else{
                logger.info(faild.getPrintThing());
                AlertScreen.showAlert(Alert.AlertType.ERROR, wrongMailOrPass.getPrintThing());
            }
        }
        else{
            AlertScreen.mandatoryFieldsNotFilled(messages);
        }
    }

    @FXML
    protected void gotToWelcomePagePageButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        HelloApplication.getMainStage().setTitle("Welcome");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }


    private List<String> isFull() {
        List<String> messages = new ArrayList<>();
        if (email.getText().isBlank()) {
            messages.add("Molimo unosite email!");
        }
        if (password.getText().isBlank()) {
            messages.add("Molim unesite lozinku");
        }
        return messages;
    }


}