package com.example.projektjava.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(DeleteException.class);

    public DeleteException() {
        logger.error("Error in the database");
    }

    public DeleteException(String message) {
        super(message);
        logger.error(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }

    public DeleteException(Throwable cause) {
        super(cause);
        logger.error("Error in the database");
    }

    public DeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.error("Error in the database");
    }
}
