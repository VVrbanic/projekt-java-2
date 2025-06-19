package com.example.projektjava.controller;
import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.exceptions.ErrorWhileReadingFileException;
import com.example.projektjava.model.Printer;
import com.example.projektjava.model.User;
import com.example.projektjava.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ReadTextFileController {
    private static final Logger logger = LoggerFactory.getLogger(ReadTextFileController.class);
    static String pathToTextFile = "C:\\Users\\Vera\\Desktop\\Java\\projekt-java\\src\\main\\java\\com\\example\\projektjava\\userInfo.txt";
    static Printer<String> noUserFound = new Printer<>("No users found in file");
    static Printer<String> errorWhileReading = new Printer<>( "Error while reading text file");
    static Printer<String> errorWhileReadingFile = new Printer<>( "Error while trying to read from text file: ");
    static Printer<String> errorWhileWritingFile = new Printer<>( "Error while trying to writing in text file: ");



    public static boolean checkIfUserExists(String email, String password) throws ErrorWhileReadingFileException {
        Set<UserInfo> usersInfo = getUsersFromFile();
        if(usersInfo.isEmpty()){
            throw new ErrorWhileReadingFileException(noUserFound.getPrintThing());
        }
        for(UserInfo user : usersInfo){
            if(user.getEmail().equals(email) && user.getPassword().equals(DataBase.hashPassword(password))){
                return true;
            }
        }
        return false;
    }

    public static Set<UserInfo> getUsersFromFile() throws ErrorWhileReadingFileException {
        setUserInfoToFile();
        Set<UserInfo> users = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToTextFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;
                String[] userInfo = line.split(",");
                if(userInfo.length == 2){
                    users.add(new UserInfo(userInfo[0], userInfo[1]));
                }else{
                    throw new ErrorWhileReadingFileException(errorWhileReading.getPrintThing());
                }
            }
        } catch (IOException ex) {
            logger.error(errorWhileReadingFile.getPrintThing() , ex.getMessage());
        }
        return users;
    }

    private static void setUserInfoToFile() {
        List<User> users = DataBase.getAllUsers();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathToTextFile))){
            for(User user : users){
                writer.write(user.getEmail() + ',' + user.getPassword());
                writer.newLine();
            }

        }catch (IOException ex){
            logger.error(errorWhileWritingFile.getPrintThing(), ex.getMessage());

        }
    }
}
