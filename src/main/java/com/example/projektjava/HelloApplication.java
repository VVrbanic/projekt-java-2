package com.example.projektjava;

import com.example.projektjava.thread.ConflictDisplayer;
import com.example.projektjava.thread.ConflictReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Map<String, Boolean> sharedConflictMap = new HashMap<>();
        Object sharedLock = new Object();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        scheduler.scheduleAtFixedRate(new ConflictReader(sharedConflictMap, sharedLock), 0, 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new ConflictDisplayer(sharedConflictMap, sharedLock), 3, 3, TimeUnit.SECONDS);

        System.out.println("Pokrenute obje niti.");
        launch();

    }
}