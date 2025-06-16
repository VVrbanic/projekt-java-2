package com.example.projektjava.controller;

import com.example.projektjava.AppConstants;
import com.example.projektjava.HelloApplication;
import com.example.projektjava.AlertScreen;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.exceptions.NoConnectionToDatabaseException;
import com.example.projektjava.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public non-sealed class LoginController implements AlertScreen {
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;
    @FXML
    public Button login;
    @FXML
    public Button goBack;

    private static final Logger logger = LoggerFactory.getLogger(NoConnectionToDatabaseException.class);
    @FXML
    protected void logInButtonClicked() throws IOException, DatabaseException {
        List<String> messages = isFull();
        if (messages.isEmpty()) {
            Optional<User> user = DataBase.getUserIfExists(email.getText(), password.getText());
            if(user.isPresent()) {
                logger.info("Login Successful");
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-about.fxml"));
                Parent root = fxmlLoader.load();
                ProfileAboutController controller = fxmlLoader.getController();
                controller.setUser(user.get());

                Scene scene = new Scene(root, 1000, 600);
                HelloApplication.getMainStage().setTitle("Welcome");
                HelloApplication.getMainStage().setScene(scene);
                HelloApplication.getMainStage().show();
            }else{
                logger.info("Login Failed");
                AlertScreen.showAlert(Alert.AlertType.ERROR, "Krivo korisničko ime ili lozinka");
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