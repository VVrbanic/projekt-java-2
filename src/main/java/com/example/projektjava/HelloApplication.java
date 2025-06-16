package com.example.projektjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
        mainStage = stage;
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}