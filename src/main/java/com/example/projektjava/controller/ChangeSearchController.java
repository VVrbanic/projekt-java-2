package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.BinaryFile;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.ChangeTypeEnum;
import com.example.projektjava.model.Change;
import com.example.projektjava.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public DatePicker date;
    @FXML
    public Button searchButton;

    @FXML
    private TableView<Change> changeTableView;
    @FXML
    private TableColumn<Change,String> oldValueColumn;
    @FXML
    private TableColumn<Change,String> newValueColumn;
    @FXML
    private TableColumn<Change,String> changeTypeColumn;
    @FXML
    private  TableColumn<Change,String> tableColumn;
    @FXML
    private TableColumn<Change,String> userColumn;
    @FXML
    private TableColumn<Change, String> dateColumn;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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
        date.setValue(LocalDate.now());

    }

    @FXML
    protected void search() {
        List<Change> changes = BinaryFile.getAllChanges();

        List<Change> filteredChanges = changes.stream()
                .filter(change ->
                        (oldValue.getText().isEmpty() || (change.getOldValue() != null && change.getOldValue().contains(oldValue.getText()))) &&
                        (newValue.getText().isEmpty() || (change.getNewValue() != null && change.getNewValue().contains(newValue.getText()))) &&
                        (table.getText().isEmpty() || (change.getTable() != null && change.getTable().contains(table.getText()))) &&
                        (user.getText().isEmpty() || (change.getUserId() != null && DataBase.getUserById(change.getUserId()).get().getFullNameAndId().contains(user.getText()))) &&
                        (date.getValue() == null || (change.getDateTime() != null && change.getDateTime().toLocalDate().equals(date.getValue()))) &&
                        (changeType.getValue() == null || (change.getChangeType() != null && change.getChangeType().equals(changeType.getValue().getName())))

                )
                .collect(Collectors.toList());

        changeTableView.setItems(FXCollections.observableList(filteredChanges));
    }


}
