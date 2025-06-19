package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.exceptions.NoConnectionToDatabaseException;
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

public class NewConflictConroller implements AlertScreen {
    @FXML
    ListView<String> listView;
    @FXML
    DatePicker date;
    @FXML
    TextField description;
    @FXML
    RadioButton anonymusYes;
    @FXML
    RadioButton anonymusNo;

    public void initialize() {
        ToggleGroup role = new ToggleGroup();
        anonymusYes.setToggleGroup(role);
        anonymusNo.setToggleGroup(role);
    }
}