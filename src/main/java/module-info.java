module com.example.projektjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.example.projektjava to javafx.fxml;
    exports com.example.projektjava;
    exports com.example.projektjava.controller;
    opens com.example.projektjava.controller to javafx.fxml;
    exports com.example.projektjava.dataBase;
    opens com.example.projektjava.dataBase to javafx.fxml;
}