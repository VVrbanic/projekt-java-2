package com.example.projektjava;

import com.example.projektjava.enums.ChangeTypeEnum;
import com.example.projektjava.model.ChangeDTO;
import com.example.projektjava.model.ConflictForm;
import com.example.projektjava.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BinaryFile {

    static UserSession session = UserSession.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(BinaryFile.class);

    public static void recordAddUser(Long newUserId) {
        ChangeDTO changeDTO = new ChangeDTO(null, String.valueOf(newUserId), AppConstants.userTable, session.getUser().getId(), LocalDateTime.now(), ChangeTypeEnum.NEW.getName());
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("logs/change.bin",  true))) {
            stream.writeObject(changeDTO);
            logger.info("Change serialized successfully.");
        } catch (IOException e) {
            logger.error("Error while serializing", e);
            e.printStackTrace();
        }
    }

    public static void recordChangeUser(User oldValue, User newValue) {
    }

    public static void recordDeleteUser(User user) {
    }

    public static void recordAddConflict(ConflictForm conflict) {
    }

    public static void recordChangeConflict(ConflictForm conflictOld, ConflictForm conflictNew) {
    }

    public static void recordDeleteConflict(ConflictForm conflict) {
    }

    public static List<ChangeDTO> getAllChanges() {
        List<ChangeDTO> changeDTOList = new ArrayList<>();
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("logs/change.bin"))) {
            while (true) {
                try {
                    ChangeDTO changeDTO = (ChangeDTO) stream.readObject();
                    logger.info("Deserialized change: " + changeDTO);
                    changeDTOList.add(changeDTO);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return changeDTOList;
    }

}
