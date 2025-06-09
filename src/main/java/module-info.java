module com.example.projektjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projektjava to javafx.fxml;
    exports com.example.projektjava;
}