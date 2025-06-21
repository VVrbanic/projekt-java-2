package com.example.projektjava.dataBase;

import com.example.projektjava.AlertScreen;
import com.example.projektjava.AppConstants;
import com.example.projektjava.exceptions.DatabaseException;
import com.example.projektjava.mapper.ConflictMapperImpl;
import com.example.projektjava.model.*;
import javafx.scene.control.Alert;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public final class DataBase {

    private static final ConflictMapperImpl mapper = new ConflictMapperImpl();

    private DataBase() {
        // private constructor to prevent instantiation
    }

    public static Connection connection() throws IOException, SQLException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(AppConstants.propFilePath)) {
            properties.load(reader);
        }
        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }
    //USER
    public static Long getNextUserId() {
        return getAllUsers().stream()
                .mapToLong(user -> user.getId())
                .max()
                .orElse(0L) + 1;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(buildUserFromResultSet(rs));
            }

        } catch (IOException | SQLException ex) {
            throw new DatabaseException("Error fetching users.", ex);
        }

        return users;
    }

    public static List<UserPrint> getAllUsersPrint() {
        List<UserPrint> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                users.add(new UserPrint(fullName, rs.getLong("id")));
            }

        } catch (IOException | SQLException ex) {
            throw new DatabaseException("Error fetching user prints.", ex);
        }

        return users;
    }

    public static Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(buildUserFromResultSet(rs)) : Optional.empty();
            }

        } catch (SQLException ex) {
            AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
        } catch (IOException ex) {
            throw new DatabaseException(ex);
        }

        return Optional.empty();
    }

    public static Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(buildUserFromResultSet(rs)) : Optional.empty();
            }

        } catch (SQLException ex) {
            AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
        } catch (IOException ex) {
            throw new DatabaseException(ex);
        }

        return Optional.empty();
    }

    public static List<User> getUsersInvolved(Long conflictId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM conflict_users WHERE conflict_id = ?";

        try (Connection conn = connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, conflictId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    getUserById(rs.getLong("user_id")).ifPresent(users::add);
                }
            }

        } catch (SQLException ex) {
            AlertScreen.showAlert(Alert.AlertType.ERROR, "Database error: " + ex.getMessage());
        } catch (IOException ex) {
            throw new DatabaseException(ex);
        }

        return users;
    }

    //CONFLICT
    public static Long getNextConflictId() {
        return getAllConflicts().stream()
                .mapToLong(conflict -> conflict.getId())
                .max()
                .orElse(0L) + 1;
    }

    public static List<ConflictForm> getAllConflicts() {
        List<ConflictDTO> conflictsDTO = new ArrayList<>();
        String query = "SELECT * FROM conflicts";

        try (Connection conn = connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                conflictsDTO.add(new ConflictDTO.Builder()
                        .id(rs.getLong("id"))
                        .reporterId(rs.getLong("reporter_id"))
                        .description(rs.getString("description"))
                        .statusId(rs.getLong("status_id"))
                        .date(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null)
                        .build());
            }

        } catch (IOException | SQLException ex) {
            throw new DatabaseException("Error fetching conflicts.", ex);
        }

        return mapper.mapDTOToForm(conflictsDTO);
    }

    public static String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }

    private static User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return new User.UserBuilder()
                .setId(rs.getLong("id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setUserName(rs.getString("user_name"))
                .setIsAdmin(rs.getLong("is_admin") != 0)
                .setEmail(rs.getString("email"))
                .setPassword(rs.getString("password"))
                .build();
    }
    //USER CONFLICT
    public static Long getNextConflictUsersId(){
        return getAllConflictUsers().stream()
                .mapToLong(conflictUser -> conflictUser.getId())
                .max()
                .orElse(0L) + 1;
    }
    private static List<ConflictUsers> getAllConflictUsers(){
        List<ConflictUsers> conflictUsers = new ArrayList<>();
        String query = "SELECT * FROM conflict_users";

        try (Connection conn = connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                conflictUsers.add(buildConflictFromResultSet(rs));
            }

        } catch (IOException | SQLException ex) {
            throw new DatabaseException("Error fetching conflict_users table.", ex);
        }

        return conflictUsers;
    }

    private static ConflictUsers buildConflictFromResultSet(ResultSet rs) throws SQLException{
        return new ConflictUsers.Builder()
                .setId(rs.getLong("id"))
                .setUserId(rs.getLong("user_id"))
                .build();
    }
}
