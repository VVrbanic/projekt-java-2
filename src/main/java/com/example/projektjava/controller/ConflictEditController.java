package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.BinaryFile;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.StatusEnum;
import com.example.projektjava.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConflictEditController implements AlertScreen {
    @FXML
    ListView<String> involved;
    @FXML
    DatePicker date;
    @FXML
    TextArea description;
    @FXML
    ChoiceBox<StatusEnum> status;

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

    private Long conflictId = null;


    private static final Logger log = LoggerFactory.getLogger(ConflictEditController.class);
    private Printer<String> reportConformation = new Printer("Jeste li sigurni da želite izmjeniti konfilkt?");
    private Printer<String> noUserSelected = new Printer("Nije ozačen niti jedan korisnik");
    private Printer<String> deleteConformation = new Printer("Jeste li sigurni da zelite obrisati konflikt?");
    private Printer<String> success = new Printer("Uspješno!");
    private ConflictFormChange oldConflict;


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
        involved.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<UserPrint> usersPrintList = DataBase.getAllUsersPrint();
        ObservableList<String> observableList = FXCollections.observableList(usersPrintList.stream()
                .map(u -> u.getName() + " (" + u.getId() + ")")
                .toList());
        involved.setItems(observableList);
        conflictTableView.setOnMouseClicked(event -> {
            if (!conflictTableView.getSelectionModel().isEmpty()) {
                ConflictForm conflictForm = conflictTableView.getSelectionModel().getSelectedItem();
                conflictId = conflictForm.getId();
                if(event.getClickCount() == 2) {
                    oldConflict = new ConflictFormChange(conflictForm.getUserInvolved().stream().map(user -> String.valueOf(user.getFullNameAndId())).collect(Collectors.toList()), conflictForm.getDescription(), conflictForm.getStatus(), conflictForm.getDate());
                    involved.getSelectionModel().clearSelection();
                    for (User involvedUser : conflictForm.getUserInvolved()) {
                        for (int i = 0; i < involved.getItems().size(); i++) {
                            String item = involved.getItems().get(i);
                            if (item.contains("(" + involvedUser.getId() + ")")) {
                                involved.getSelectionModel().select(i);
                            }
                        }
                    }
                    date.setValue(conflictForm.getDate());
                    description.setText(conflictForm.getDescription());
                    status.setValue(conflictForm.getStatus());
                }
            }
        });
    }
    @FXML
    protected void save() {
        List<String> messages = isFull();
        if(messages.isEmpty()) {
            List<Long> userIdList = exctactUserIdfromList(involved.getSelectionModel().getSelectedItems());
            String descriptionS = description.getText();
            LocalDate dateS = date.getValue();
            Long statusLong = status.getValue().getId();
            ConflictFormChange newConflictChange = new ConflictFormChange(involved.getSelectionModel().getSelectedItems(), descriptionS, StatusEnum.getNameById(statusLong), dateS);
            if(AlertScreen.conformation(reportConformation.getPrintThing())) {
                try (Connection con = DataBase.connection()) {
                    String sql = "UPDATE conflicts SET description = ?, status_id = ?, date = ?  WHERE id = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, descriptionS);
                    stmt.setLong(2, statusLong);
                    stmt.setDate(3, Date.valueOf(dateS));
                    stmt.setLong(4, conflictId);
                    stmt.executeUpdate();

                    String sql3 = "DELETE FROM conflict_users WHERE conflict_id = ?";
                    PreparedStatement stmt3 = con.prepareStatement(sql3);
                    stmt3.setLong(1, conflictId);
                    stmt3.executeUpdate();

                    Long conflictUsersId = DataBase.getNextConflictUsersId();
                    for (Long userId : userIdList) {
                        String sql2 = "INSERT INTO conflict_users (id, conflict_id, user_id) VALUES (?, ?, ?)";
                        PreparedStatement stmt2 = con.prepareStatement(sql2);
                        stmt2.setLong(1, conflictUsersId);
                        stmt2.setLong(2, conflictId);
                        stmt2.setLong(3, userId);
                        stmt2.executeUpdate();
                        conflictUsersId++;
                    }
                    BinaryFile.recordChangeConflict(oldConflict, newConflictChange);
                    AlertScreen.info(success.getPrintThing());

                } catch (SQLException | IOException e) {
                    AlertScreen.error(AppConstants.errorGlobal);
                    throw new RuntimeException(e);
                }
            }
        }else{
            AlertScreen.mandatoryFieldsNotFilled(messages);
        }
    }

    private List<Long> exctactUserIdfromList(List<String> list){
        List<Long> userIdList = new ArrayList<>();
        if(list.isEmpty()){
            log.error(noUserSelected.getPrintThing());
        }
        else {
            for (String l : list) {
                Matcher matcher = Pattern.compile("\\((\\d+)\\)").matcher(l);
                if (matcher.find()) {
                    userIdList.add(Long.parseLong(matcher.group(1)));
                }
            }
        }
        return userIdList;
    }

    private List<String> isFull() {
        List<String> messages = new ArrayList<>();
        if (involved.getSelectionModel().getSelectedItems().isEmpty()) {
            messages.add("Molimo označite tko je sve sudjelovao u konfliktu");
        }
        if(date.getValue().isAfter(LocalDate.now())){
            messages.add("Datum događaja ne može biti u budućnosti");
        }
        if(date.getValue() == null){
            messages.add("Molim unijeti datum događaja");
        }
        if (description.getText().isBlank()) {
            messages.add("Molim opis dogadaja");
        }
        return messages;
    }

    @FXML
    protected void delete(){
       if(AlertScreen.conformation(deleteConformation.getPrintThing())) {
           try (Connection con = DataBase.connection()) {
               String sql = "DELETE FROM conflicts WHERE id = ?";
               PreparedStatement stmt = con.prepareStatement(sql);
               stmt.setLong(1, conflictId);
               stmt.executeUpdate();
           } catch (SQLException | IOException e) {
               AlertScreen.error(AppConstants.errorGlobal);
               throw new RuntimeException(e);
           }
           try (Connection con = DataBase.connection()) {
               String sql2 = "DELETE FROM conflict_users WHERE conflict_id = ?";
               PreparedStatement stmt2 = con.prepareStatement(sql2);
               stmt2.setLong(1, conflictId);
               stmt2.executeUpdate();
           } catch (SQLException | IOException e) {
               AlertScreen.error(AppConstants.errorGlobal);
               throw new RuntimeException(e);
           }
           BinaryFile.recordDelete(conflictId, AppConstants.conflictTable);
           AlertScreen.info(success.getPrintThing());
           List<ConflictForm> allConflicts = DataBase.getAllConflicts();
           conflictTableView.setItems(FXCollections.observableList(allConflicts));
       }
    }

}