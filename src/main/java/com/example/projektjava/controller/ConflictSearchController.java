package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.model.ConflictForm;
import com.example.projektjava.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConflictSearchController implements AlertScreen {
    @FXML
    public ChoiceBox involved;
    @FXML
    public ChoiceBox reportedBy;
    @FXML
    public DatePicker date;
    @FXML
    public ChoiceBox status;

    @FXML
    private TableView<ConflictForm> conflictTableView;
    @FXML
    private TableColumn<ConflictForm, String> idColumn;
    @FXML
    private TableColumn<ConflictForm,String> descriptionColumn;
    @FXML
    private TableColumn<ConflictForm,String> reportedByColumn;
    @FXML
    private TableColumn<ConflictForm,String> dateColumn;
    @FXML
    private  TableColumn<ConflictForm,String> involvedColumn;
    @FXML
    private TableColumn<ConflictForm,String> statusColumn;

    UserSession session = UserSession.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ConflictSearchController.class);
    public void initialize() {
        date.setValue(LocalDate.now());

        conflictTableView.setItems(FXCollections.observableList(DataBase.getAllConflicts()));
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        reportedByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReporter() != null ? cellData.getValue().getReporter().getFirstName() +" "+ cellData.getValue().getReporter().getLastName() : AppConstants.reporteUnknown));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate())));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        involvedColumn.setCellValueFactory(cellData -> {
                    List<User> users = cellData.getValue().getUserInvolved();
                    String names = users.stream()
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .collect(Collectors.joining(", "));
                    return new SimpleStringProperty(names);
                }
            );
        //dodaj dropdowne
    }

    @FXML
    protected void search() {
    }

    @FXML
    protected void clear() {
    }
}