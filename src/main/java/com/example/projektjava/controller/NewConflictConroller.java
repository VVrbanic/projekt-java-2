package com.example.projektjava.controller;

import com.example.projektjava.AlertScreen;

import com.example.projektjava.UserSession;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.enums.StatusEnum;
import com.example.projektjava.model.Printer;
import com.example.projektjava.model.UserPrint;
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

public class NewConflictConroller implements AlertScreen {
    private static final Logger log = LoggerFactory.getLogger(NewConflictConroller.class);
    @FXML
    ListView<String> listView;
    @FXML
    DatePicker date;
    @FXML
    TextArea description;
    @FXML
    RadioButton anonymusYes;
    @FXML
    RadioButton anonymusNo;

    private Printer<String> noUserSelected = new Printer("Nije ozačen niti jedan korisnik");
    private Printer<String> reportConformation = new Printer("Are you sure you want to report this?");


    public void initialize() {
        ToggleGroup role = new ToggleGroup();
        anonymusYes.setToggleGroup(role);
        anonymusNo.setToggleGroup(role);
        date.setValue(LocalDate.now());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<UserPrint> usersPrintList = DataBase.getAllUsersPrint();
        ObservableList<String> observableList = FXCollections.observableList(usersPrintList.stream()
                .map(u -> u.getName() + " (" + u.getId() + ")")
                .toList());
        listView.setItems(observableList);
    }

    @FXML
    protected void save() throws SQLException, IOException {
        List<String> messages = isFull();
        if(messages.isEmpty()) {
            Long reporterId = null;
            Long id = DataBase.getNextConflictId();
            List<Long> userIdList = exctactUserIdfromList(listView.getSelectionModel().getSelectedItems());
            if(anonymusNo.isSelected()) {
                reporterId = UserSession.getInstance().getUser().getId();
            }
            String descriptionS = description.getText();
            LocalDate dateS = date.getValue();
            Long statusLong = StatusEnum.NEW.getId();
            if (AlertScreen.conformation(reportConformation.getPrintThing())) {
                try (Connection con = DataBase.connection()) {
                    String sql = "INSERT INTO conflicts (id, reporter_id, description, status_id, date) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setLong(1, id);
                    if (reporterId != null) {
                        stmt.setLong(2, reporterId);
                    } else {
                        stmt.setNull(2, java.sql.Types.BIGINT);
                    }
                    stmt.setString(3, descriptionS);
                    stmt.setLong(4, statusLong);
                    stmt.setDate(5, Date.valueOf(dateS));
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try (Connection con = DataBase.connection()) {
                    Long conflictUsersId = DataBase.getNextConflictUsersId();
                    for (Long userId : userIdList) {
                        String sql = "INSERT INTO conflict_users (id, conflict_id, user_id) VALUES (?, ?, ?)";
                        PreparedStatement stmt = con.prepareStatement(sql);
                        stmt.setLong(1, conflictUsersId);
                        stmt.setLong(2, id);
                        stmt.setLong(3, userId);
                        stmt.executeUpdate();
                        conflictUsersId++;
                    }

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
        if (listView.getSelectionModel().getSelectedItems().isEmpty()) {
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
        if(!(anonymusYes.isSelected() ||  anonymusNo.isSelected())){
            messages.add("Molim označite želite li da prijava bude anonimna");
        }
        return messages;
    }

}