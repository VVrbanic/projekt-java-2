package com.example.projektjava.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseException.class);

    public DatabaseException(String message) {
        super(message);
        logger.error(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
        logger.error("Error in the database");
    }

}
