package com.invt.tech.handler;

/**
 * Runtime exception thrown when an error occurs during CSV export operations.
 */
public class CSVExportException extends RuntimeException {

    /**
     * Constructs a new CSVExportException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause the underlying cause of the exception
     */
    public CSVExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
