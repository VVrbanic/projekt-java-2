package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.HelloApplication;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.exceptions.DeleteException;
import com.example.projektjava.exceptions.NoConnectionToDatabaseException;
import com.example.projektjava.model.User;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersEditController implements AlertScreen {
    private Long id;
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
    public Button saveButton;
    @FXML
    public Button deleteButton;

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User,String> firstNameColumn;
    @FXML
    private TableColumn<User,String> lastNameColumn;
    @FXML
    private TableColumn<User,String> userNameColumn;
    @FXML
    private  TableColumn<User,String> emailColumn;
    @FXML
    private TableColumn<User,String> isAdminColumn;

    UserSession session = UserSession.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(NoConnectionToDatabaseException.class);
    public void initialize() {
        ToggleGroup role = new ToggleGroup();
        roleUser.setToggleGroup(role);
        roleAdmin.setToggleGroup(role);
        userTableView.setItems(FXCollections.observableList(DataBase.getAllUsers()));
        userIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        firstNameColumn.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getLastName()));
        userNameColumn.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getUserName()));
        emailColumn.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getEmail()));
        isAdminColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().isAdmin())));
        userTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !userTableView.getSelectionModel().isEmpty()) {
                User selectedUser = userTableView.getSelectionModel().getSelectedItem();
                firstName.setText(selectedUser.getFirstName());
                lastName.setText(selectedUser.getLastName());
                email.setText(selectedUser.getEmail());
                userName.setText(selectedUser.getUserName());
                if(selectedUser.isAdmin()){
                    roleAdmin.setSelected(true);
                    roleUser.setSelected(false);
                }else{
                    roleUser.setSelected(true);
                    roleAdmin.setSelected(false);
                }
                id = selectedUser.getId();

            }
            else if (!userTableView.getSelectionModel().isEmpty()) {
                User selectedUser = userTableView.getSelectionModel().getSelectedItem();
                id = selectedUser.getId();
            }
        });

    }

    @FXML
    protected void save() {
        List<String> messages = isFull();
        if (messages.isEmpty()) {
            String first = firstName.getText();
            String last = lastName.getText();
            String user = userName.getText();
            String mail = email.getText();
            Long role = roleAdmin.isSelected() ? AppConstants.TRUE : AppConstants.FALSE;


            try (Connection conn = DataBase.connection()) {
                String sql = "UPDATE users SET first_name = ?, last_name = ?, user_name = ?, email = ?, is_admin = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, first);
                stmt.setString(2, last);
                stmt.setString(3, user);
                stmt.setString(4, mail);
                stmt.setBoolean(5, Objects.equals(role, AppConstants.TRUE));
                stmt.setLong(6, id);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    logger.info("User updated successfully.");
                    clearAll();
                    ObservableList<User> updatedList = FXCollections.observableArrayList(DataBase.getAllUsers());
                    userTableView.setItems(updatedList);
                } else {
                    logger.error("No user found with ID: {}", id);
                }
            } catch (SQLException ex) {
                AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
                clearAll();
            } catch (IOException e) {
                throw new DatabaseException(e);
            }
        }
        else{
            AlertScreen.mandatoryFieldsNotFilled(messages);
        }
    }

    @FXML
    protected void delete() {
        if(AlertScreen.conformation("Jeste sigurni da želite izbrisati korisnika?")) {
            if (!Objects.equals(id, session.getUser().getId())) {
                try (Connection conn = DataBase.connection()) {
                    String sql = "DELETE FROM users WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setLong(1, id);

                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            logger.info("User deleted successfully.");
                            clearAll();
                            ObservableList<User> updatedList = FXCollections.observableArrayList(DataBase.getAllUsers());
                            userTableView.setItems(updatedList);
                        } else {
                            logger.info("No user found with ID: {}", id);
                        }
                    } catch (DatabaseException e) {
                        throw new DatabaseException("Nije moguće doći do korinika.");
                    }
                } catch (SQLException | IOException | DatabaseException e) {
                    logger.error("Error while deleting user with ID: {}", id);
                }
            } else {
                AlertScreen.showAlert(Alert.AlertType.ERROR, "Ne možete izbrisati samog sebe!");
                logger.error("Error - cannot erase yourself!");
                throw new DeleteException("Ne možete izbrisati sebe!");
            }
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

    private void clearAll() {
        firstName.clear();
        lastName.clear();
        userName.clear();
        email.clear();
        roleUser.setSelected(false);
        roleAdmin.setSelected(false);
    }
}