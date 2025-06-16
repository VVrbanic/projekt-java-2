package com.example.projektjava.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoConnectionToDatabaseException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(NoConnectionToDatabaseException.class);

    public NoConnectionToDatabaseException() {
        logger.error("No database connection could not be established");
    }

    public NoConnectionToDatabaseException(String message) {
        super(message);
        logger.error(message);
    }

    public NoConnectionToDatabaseException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }

    public NoConnectionToDatabaseException(Throwable cause) {
        super(cause);
        logger.error("No database connection could not be established");
    }

    public NoConnectionToDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.error("No database connection could not be established");
    }
}
