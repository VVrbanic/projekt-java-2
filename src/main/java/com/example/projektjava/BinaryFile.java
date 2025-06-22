package com.example.projektjava;

import com.example.projektjava.enums.ChangeTypeEnum;
import com.example.projektjava.model.ChangeDTO;

import com.example.projektjava.model.ConflictFormChange;
import com.example.projektjava.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class BinaryFile {

    static UserSession session = UserSession.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(BinaryFile.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void recordAdd(Long id, String table) {
        ChangeDTO changeDTO = new ChangeDTO(null, String.valueOf(id), table, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.NEW.getName());
        addLog(changeDTO);
    }

    public static void recordDelete(Long id, String table) {
        ChangeDTO changeDTO = new ChangeDTO(String.valueOf(id),null, table, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.DELETE.getName());
        addLog(changeDTO);
    }

    public static void recordChangeUser(User oldValue, User newValue) {
        if(!Objects.equals(oldValue.getFirstName(), newValue.getFirstName())){
            addLog(new ChangeDTO(oldValue.getFirstName(), newValue.getFirstName(), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(!Objects.equals(oldValue.getLastName(), newValue.getLastName())){
            addLog(new ChangeDTO(oldValue.getLastName(), newValue.getLastName(), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(!Objects.equals(oldValue.getUserName(), newValue.getUserName())){
            addLog(new ChangeDTO(oldValue.getUserName(), newValue.getUserName(), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(oldValue.isAdmin() != newValue.isAdmin()){
            addLog(new ChangeDTO(String.valueOf(oldValue.isAdmin() ? AppConstants.TRUE : AppConstants.FALSE), String.valueOf(newValue.isAdmin() ? AppConstants.TRUE : AppConstants.FALSE), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(!Objects.equals(oldValue.getEmail(), newValue.getEmail())){
            addLog(new ChangeDTO(oldValue.getEmail(), newValue.getEmail(), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
    }

    public static void recordChangeConflict(ConflictFormChange oldValue, ConflictFormChange newValue) {
        String newInvolved = newValue.getUserInvolved().stream()
                .collect(Collectors.joining(","));
        String oldInvolved = oldValue.getUserInvolved().stream()
                .collect(Collectors.joining(","));
        if(newInvolved != oldInvolved){
            addLog(new ChangeDTO(oldInvolved, newInvolved, AppConstants.conflictTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(!Objects.equals(oldValue.getDescription(), newValue.getDescription())){
            addLog(new ChangeDTO(oldValue.getDescription(), newValue.getDescription(), AppConstants.conflictTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(oldValue.getStatus() != newValue.getStatus()){
            addLog(new ChangeDTO(oldValue.getStatus().getName(), newValue.getStatus().getName(), AppConstants.conflictTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
        if(oldValue.getDate() != newValue.getDate()){
            addLog(new ChangeDTO(formatter.format(oldValue.getDate()), formatter.format(newValue.getDate()), AppConstants.conflictTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.EDIT.getName()));
        }
    }

    private static void addLog(ChangeDTO changeDTO){
        List<ChangeDTO> changes = getAllChanges();
        changes.add(changeDTO);
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("logs/change.dat"))) {
            stream.writeObject(changes);
            logger.info("Change serialized successfully.");
        } catch (IOException e) {
            logger.error("Error while serializing", e);
            e.printStackTrace();
        }

    }

    public static List<ChangeDTO> getAllChanges() {
        File file = new File(AppConstants.binaryFilePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(AppConstants.binaryFilePath))) {
            Object obj = stream.readObject();
            if (obj instanceof List) {
                List<ChangeDTO> changeDTOList = (List<ChangeDTO>) obj;
                logger.info("Deserialized " + changeDTOList.size() + " changes.");
                return changeDTOList;
            } else {
                logger.warn("File contains an object that is not a ChangeDTO.");
                return new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            AlertScreen.error(AppConstants.errorGlobal);
            logger.error("Error while deserializing changes", e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
