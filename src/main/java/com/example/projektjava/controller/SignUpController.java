package com.example.projektjava.controller;

import com.example.projektjava.*;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.model.Printer;
import com.example.projektjava.model.User;
import com.example.projektjava.model.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SignUpController implements AlertScreen {
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField userName;
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;
    @FXML
    public PasswordField confirmPassword;
    @FXML
    public RadioButton roleAdmin;
    @FXML
    public RadioButton roleUser;
    @FXML
    public Button signUpButton;
    @FXML
    public Button returnToWelcomePageButton;
    @FXML
    private Node menuBar;


    UserSession session = UserSession.getInstance();
    Printer<String> success = new Printer<>( "User registered successfully, please log in!");
    Printer<String> successShort = new Printer<>( "User registered successfully");
    Printer<String> emailExists = new Printer<>( "Email već postoji u bazi");

    public void initialize() {
        ToggleGroup role = new ToggleGroup();
        roleUser.setToggleGroup(role);
        roleAdmin.setToggleGroup(role);
        if(session == null){
            returnToWelcomePageButton.setVisible(true);
            menuBar.setVisible(false);
            menuBar.setManaged(false);
        }else{
            returnToWelcomePageButton.setVisible(false);
            menuBar.setVisible(true);
            menuBar.setManaged(true);
        }

    }

    @FXML
    protected void signUpButtonClicked() {
        List<String> messages = isFull();
        if (messages.isEmpty()) {
            String first = firstName.getText();
            String last = lastName.getText();
            String user = userName.getText();
            String mail = email.getText();
            String pass = password.getText();
            String confirm = confirmPassword.getText();
            Long role = roleAdmin.isSelected() ? AppConstants.TRUE : AppConstants.FALSE;

            if (!pass.equals(confirm)) {
                AlertScreen.passwordsDontMatch();
                return;
            }
            if(checkMail(email.getText())){
                AlertScreen.error(emailExists.getPrintThing());
                return;
            }

            try (Connection conn = DataBase.connection()) {
                String sql = "INSERT INTO users (id, first_name, last_name, user_name, password, is_admin, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
                Long id = DataBase.getNextUserId();
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, id);
                stmt.setString(2, first);
                stmt.setString(3, last);
                stmt.setString(4, user);
                stmt.setString(5, DataBase.hashPassword(pass));
                stmt.setLong(6, role);
                stmt.setString(7, mail);

                stmt.executeUpdate();
                if(session == null){
                    AlertScreen.showAlert(Alert.AlertType.INFORMATION, success.getPrintThing());
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                    HelloApplication.getMainStage().setTitle("Welcome");
                    HelloApplication.getMainStage().setScene(scene);
                    HelloApplication.getMainStage().show();
                }else{
                    BinaryFile.recordAdd(id, AppConstants.userTable);
                    AlertScreen.showAlert(Alert.AlertType.INFORMATION, successShort.getPrintThing());
                    clearAll();
                }


            } catch (SQLException ex) {
                AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
                clearAll();
            } catch (IOException | DatabaseException e) {
                throw new DatabaseException(e);
            }
        }
        else{
            AlertScreen.mandatoryFieldsNotFilled(messages);
        }
    }

    public List<String> isFull() {
        List<String> messages = new ArrayList<>();
        if (firstName.getText().isBlank()) {
            messages.add("Molimo unesite ime");
        }
        if (lastName.getText().isBlank()) {
            messages.add("Molimo unesite prezime");
        }
        if (email.getText().isBlank()) {
            messages.add("Molimo unesite mail");
        }
        if (userName.getText().isBlank()) {
            messages.add("Molimo unosite korisničko ime!");
        }
        if (password.getText().isBlank()) {
            messages.add("Molim unesite lozinku");
        }
        if (roleAdmin.getText() == null || roleUser.getText() == null) {
            messages.add("Molimo označi rolu");
        }
        return messages;
    }

    private void clearAll() {
        firstName.clear();
        lastName.clear();
        userName.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
        roleUser.setSelected(false);
        roleAdmin.setSelected(false);

    }

    private boolean checkMail(String email){
        Optional<User> user = DataBase.getUserByEmail(email);
        if(user.isPresent()){
            return true;
        }
        return false;
    }
    @FXML
    protected void gotToWelcomePagePageButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        HelloApplication.getMainStage().setTitle("Welcome");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
}