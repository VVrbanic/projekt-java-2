package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.BinaryFile;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.ChangeTypeEnum;
import com.example.projektjava.enums.StatusEnum;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.model.ChangeDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChangeSearchController implements AlertScreen {
    @FXML
    public TextField oldValue;
    @FXML
    public TextField newValue;
    @FXML
    public ChoiceBox<ChangeTypeEnum> changeType;
    @FXML
    public TextField table;
    @FXML
    public TextField user;
    @FXML
    public Date date;
    @FXML
    public Button searchButton;

    @FXML
    private TableView<ChangeDTO> changeTableView;
    @FXML
    private TableColumn<ChangeDTO,String> oldValueColumn;
    @FXML
    private TableColumn<ChangeDTO,String> newValueColumn;
    @FXML
    private TableColumn<ChangeDTO,String> changeTypeColumn;
    @FXML
    private  TableColumn<ChangeDTO,String> tableColumn;
    @FXML
    private TableColumn<ChangeDTO,String> userColumn;
    @FXML
    private TableColumn<ChangeDTO, String> dateColumn;

    UserSession session = UserSession.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(ChangeSearchController.class);

    public void initialize() {
        changeTableView.setItems(FXCollections.observableList(BinaryFile.getAllChanges()));
        oldValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOldValue()));
        newValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewValue()));
        tableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTable()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime().format(formatter)));
        userColumn.setCellValueFactory(cellData -> {
                Optional<User> user = DataBase.getUserById(cellData.getValue().getUserId());
                String name = user.get().getFirstName() + " " + user.get().getLastName() + "( " + user.get().getId() + ") ";
                return new SimpleStringProperty(name);
                });
        changeTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChangeType()));
        changeType.getItems().addAll(ChangeTypeEnum.values());
    }

    @FXML
    protected void search() {
    }

}