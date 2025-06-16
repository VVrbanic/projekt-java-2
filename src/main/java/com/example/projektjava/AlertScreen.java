package com.example.projektjava;

import com.example.projektjava.controller.LoginController;
import com.example.projektjava.controller.ProfileAboutController;
import com.example.projektjava.controller.SignUpController;
import javafx.scene.control.Alert;

import java.util.List;

public sealed interface AlertScreen permits LoginController, ProfileAboutController, SignUpController {
    static void userExists() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText("Korisničko ime ili lozinka već postoje!");
        alert.showAndWait();
    }

    static void mandatoryFieldsNotFilled(List<String> messages) {
        String message = String.join("\n", messages);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static void passwordsDontMatch() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText("Lozinke se ne podudaraju!");
        alert.showAndWait();
    }


    static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    default void success() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Čestitamo!");
        alert.setHeaderText(null);
        alert.setContentText("Uspješno kreirano!");
        alert.showAndWait();
    }
    default void error(String m){
        var alert = new Alert(Alert.AlertType.ERROR, m);
        alert.setTitle("Greška pri unosu!");
        alert.show();
    }
}
