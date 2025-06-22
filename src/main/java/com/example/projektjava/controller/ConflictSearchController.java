package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.StatusEnum;
import com.example.projektjava.model.ConflictForm;
import com.example.projektjava.model.User;
import com.example.projektjava.model.UserPrint;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConflictSearchController implements AlertScreen {
    @FXML
    public ChoiceBox<String> involved;
    @FXML
    public ChoiceBox<String> reportedBy;
    @FXML
    public DatePicker date;
    @FXML
    public ChoiceBox<StatusEnum> status;
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


    Pattern pattern = Pattern.compile("\\((\\d+)\\)");

    public void initialize() {
        date.setValue(LocalDate.now());

        conflictTableView.setItems(FXCollections.observableList(DataBase.getAllConflicts()));
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        reportedByColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReporter() != null ?
                cellData.getValue().getReporter().getFirstName() +" "+ cellData.getValue().getReporter().getLastName()+ " (" + cellData.getValue().getReporter().getId() + ") " :
                AppConstants.reporteUnknown));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate())));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        involvedColumn.setCellValueFactory(cellData -> {
                    List<User> users = cellData.getValue().getUserInvolved();
                    String names = users.stream()
                            .map(user -> user.getFirstName() + " " + user.getLastName() + " (" + user.getId() + ") ")
                            .collect(Collectors.joining(", "));
                    return new SimpleStringProperty(names);
                }
            );
        status.getItems().addAll(StatusEnum.values());
        List<UserPrint> users = DataBase.getAllUsersPrint();
        List<String> usersList = users.stream()
                .map(u -> u.getName() + " (" + u.getId() + ")")
                .toList();
        involved.getItems().addAll(usersList);
        reportedBy.getItems().addAll(usersList);
    }

    @FXML
    protected void search() {
        List<ConflictForm> allConflicts = DataBase.getAllConflicts();
        //dodati filtriranje
        LocalDate dateS = date.getValue();
        String involvedId;
        if (involved.getValue() != null) {
            Matcher matcherInvolvedId = pattern.matcher(involved.getValue());
            if (matcherInvolvedId.find()) {
                involvedId = matcherInvolvedId.group(1);
            } else {
                involvedId = null;
            }
        } else {
            involvedId = null;
        }
        String reportedById;
        if (reportedBy.getValue() != null) {
            Matcher matcherReportedById = pattern.matcher(reportedBy.getValue());
            if (matcherReportedById.find()) {
                reportedById = matcherReportedById.group(1);
            } else {
                reportedById = null;
            }
        } else {
            reportedById = null;
        }
        Long statusS = status.getValue() != null ? status.getValue().getId() : null;
        List<ConflictForm> filteredConflicts = allConflicts.stream()
                .filter(conflict ->
                        (dateS == null || (conflict.getDate() != null && conflict.getDate().equals(dateS))) &&
                        (statusS == null || (conflict.getStatus() != null && conflict.getStatus().getId().equals(statusS))) &&
                        (reportedById == null || (conflict.getReporter() != null &&
                                conflict.getReporter().getId() != null &&
                                conflict.getReporter().getId().equals(Long.valueOf(reportedById)))) &&
                        (involvedId == null || (conflict.getUserInvolved() != null &&
                                conflict.getUserInvolved().stream()
                                        .anyMatch(user -> user != null &&
                                                user.getId() != null &&
                                                user.getId().equals(Long.valueOf(involvedId)))))
                )
                .collect(Collectors.toList());
        conflictTableView.setItems(FXCollections.observableArrayList(filteredConflicts));

    }

    @FXML
    protected void clear() {
        status.setValue(null);
        date.setValue(null);
        involved.setValue(null);
        reportedBy.setValue(null);
    }
}