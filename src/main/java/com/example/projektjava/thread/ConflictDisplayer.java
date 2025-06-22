package com.example.projektjava.thread;

import com.example.projektjava.AlertScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ConflictDisplayer implements Runnable {
    private final Map<String, Boolean> conflictMap;
    private final Object lock;
    private static final Logger logger = LoggerFactory.getLogger(ConflictDisplayer.class);

    public ConflictDisplayer(Map<String, Boolean> conflictMap, Object lock) {
        this.conflictMap = conflictMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("Usao u ConflictDisplayer");

            for (Map.Entry<String, Boolean> entry : conflictMap.entrySet()) {
                String conflictString = entry.getKey();
                Boolean isDisplayed = entry.getValue();

                if (!isDisplayed) {
                    logger.info("Prikazujem konflikt: " + conflictString);
                    AlertScreen.info("Novi konflikt! " + conflictString);
                    conflictMap.put(conflictString, true);
                    logger.info("Status konflikta promijenjen na true");
                }
            }
            System.out.println("Izasao iz displayera");
        }
    }
}
