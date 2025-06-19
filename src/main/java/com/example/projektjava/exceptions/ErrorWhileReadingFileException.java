package com.example.projektjava.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorWhileReadingFileException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ErrorWhileReadingFileException.class);

    public ErrorWhileReadingFileException() {
        logger.error("Error while reading file");
    }

    public ErrorWhileReadingFileException(String message) {
        super(message);
        logger.error(message);
    }

    public ErrorWhileReadingFileException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }

    public ErrorWhileReadingFileException(Throwable cause) {
        super(cause);
        logger.error("Error while reading file");
    }

    public ErrorWhileReadingFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.error("Error while reading file");
    }
}
