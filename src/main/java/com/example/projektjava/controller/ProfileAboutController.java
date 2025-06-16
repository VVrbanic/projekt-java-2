package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.HelloApplication;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public non-sealed class ProfileAboutController implements AlertScreen {
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField userName;
    @FXML
    public TextField email;
    @FXML
    public RadioButton roleAdmin;
    @FXML
    public RadioButton roleUser;
    @FXML
    public Button editButton;
    @FXML
    public Button saveButton;

    private String password;
    private Long id;

    public void setUser(User user) {
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        userName.setText(user.getUserName());
        email.setText(user.getEmail());
        roleUser.setSelected(!user.isAdmin());
        roleAdmin.setSelected(user.isAdmin());
        password  = user.getPassword();
        id = user.getId();
    }

    public void initialize() {
        ToggleGroup role = new ToggleGroup();
        roleUser.setToggleGroup(role);
        roleAdmin.setToggleGroup(role);
        setDisabled(true);
    }

    @FXML
    protected void saveButtonClicked() throws DatabaseException {
        List<String> messages = isFull();
        if (messages.isEmpty()) {
            String first = firstName.getText();
            String last = lastName.getText();
            String user = userName.getText();
            String mail = email.getText();
            Long role = roleAdmin.isSelected() ? AppConstants.TRUE : AppConstants.FALSE;

            try (Connection conn = DataBase.connection()) {
                String sql = "UPDATE users SET first_name = ?, last_name = ?, user_name = ?, is_admin = ?, email = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, first);
                stmt.setString(2, last);
                stmt.setString(3, user);
                stmt.setLong(4, role);
                stmt.setString(5, mail);
                stmt.setString(6, String.valueOf(id));

                stmt.executeUpdate();

                AlertScreen.showAlert(Alert.AlertType.INFORMATION, "User registered successfully, please log in!");
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                HelloApplication.getMainStage().setTitle("Welcome");
                HelloApplication.getMainStage().setScene(scene);
                HelloApplication.getMainStage().show();

            } catch (SQLException ex) {
                AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
            } catch (IOException e) {
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
        if (roleAdmin.getText() == null || roleUser.getText() == null) {
            messages.add("Molimo označi rolu");
        }
        return messages;
    }

    @FXML
    protected void editButtonClicked() {
        setDisabled(false);
    }

    private void setDisabled(boolean isDisabled) {
        if (isDisabled) {
            firstName.setDisable(true);
            lastName.setDisable(true);
            userName.setDisable(true);
            email.setDisable(true);
            roleAdmin.setDisable(true);
            roleUser.setDisable(true);
        }else{
            firstName.setDisable(false);
            lastName.setDisable(false);
            userName.setDisable(false);
            email.setDisable(false);
            roleAdmin.setDisable(false);
            roleUser.setDisable(false);

        }
    }
}