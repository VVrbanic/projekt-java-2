package com.example.projektjava.thread;

import com.example.projektjava.dataBase.DataBase;
import com.example.projektjava.model.ConflictForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;

public class ConflictReader implements Runnable {
    private final Map<String, Boolean> conflictMap;
    private final Object lock;
    private static final Logger logger = LoggerFactory.getLogger(ConflictReader.class);

    public ConflictReader(Map<String, Boolean> conflictMap, Object lock) {
        this.conflictMap = conflictMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("Usao u ConflictReader");

            List<ConflictForm> newConflicts = DataBase.getAllConflictsUnresolved();

            for (ConflictForm conflict : newConflicts) {
                String combinedString = conflict.getDate() + " - " + conflict.getDescription();
                if (!conflictMap.containsKey(combinedString)) {
                    conflictMap.put(combinedString, false);
                    logger.info("Dodao novi konflikt: " + combinedString);
                }
            }
        }
    }
}
