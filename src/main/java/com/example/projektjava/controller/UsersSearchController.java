package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersSearchController implements AlertScreen {
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
    public Button searchButton;

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
    private static final Logger logger = LoggerFactory.getLogger(UsersSearchController.class);
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
    }

    @FXML
    protected void search() {
        String first = firstName.getText();
        String last = lastName.getText();
        String user = userName.getText();
        String mail = email.getText();
        Long role = roleAdmin.isSelected() ? AppConstants.TRUE : AppConstants.FALSE;

        try (Connection conn = DataBase.connection()) {
            String sql = "SELECT * FROM users WHERE 1=1"
                    + " AND first_name LIKE ?"
                    + " AND last_name LIKE ?"
                    + " AND user_name LIKE ?"
                    + " AND email LIKE ?"
                    + " AND is_admin = ?"
                    + "AND active = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + first + "%");
            stmt.setString(2, "%" + last + "%");
            stmt.setString(3, "%" + user + "%");
            stmt.setString(4, "%" + mail + "%");
            stmt.setBoolean(5, Objects.equals(role, AppConstants.TRUE));
            stmt.setLong(6, AppConstants.TRUE);
            stmt.executeQuery();
            ResultSet rs = stmt.executeQuery();
            List<User> matchedUsers = new ArrayList<>();

            while (rs.next()) {
                matchedUsers.add(new User.UserBuilder()
                        .setId(rs.getLong("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUserName(rs.getString("user_name"))
                        .setEmail(rs.getString("email"))
                        .setIsAdmin(rs.getBoolean("is_admin"))
                        .build());
            }

            userTableView.setItems(FXCollections.observableArrayList(matchedUsers));

        } catch (SQLException ex) {
            AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

}