package com.example.projektjava.dataBase;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.model.User;
import javafx.scene.control.Alert;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DataBase {

    public static Connection connection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader(AppConstants.propFilePath));
        return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
    }

    public static Long getNextUserId() throws DatabaseException {
        List<User> user = getAllUsers();
        return (long) (user.size() + 1);
    }

    public static List<User> getAllUsers() throws DatabaseException {
        try (Connection conn = DataBase.connection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            List<User> users = new ArrayList<>();
            while(rs.next()){
                users.add(new User.UserBuilder()
                        .setId(rs.getLong("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUserName(rs.getString("user_name"))
                        .setIsAdmin(rs.getLong("is_admin") != 0)
                        .setEmail(rs.getString("email"))
                        .build());
            }
            return  users;

        }catch (IOException | SQLException ex){
            throw new DatabaseException("Getting users error!", ex);

        }

    }

    public static Optional<User> getUserIfExists(String email, String password) throws DatabaseException {
        try (Connection conn = DataBase.connection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, DataBase.hashPassword(password));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Optional.ofNullable(new User.UserBuilder()
                        .setId(rs.getLong("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUserName(rs.getString("user_name"))
                        .setIsAdmin(rs.getLong("is_admin") != 0)
                        .setEmail(rs.getString("email"))
                        .build());

            }
            else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }
    //ovo treba premjestiti u nekom trenu
    public static String hashPassword(String password){
        return String.valueOf(password.hashCode());
    }
}
