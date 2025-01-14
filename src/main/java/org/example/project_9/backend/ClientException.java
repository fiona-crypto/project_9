package org.example.project_9.backend;

/**
 * Custom exception to handle errors that occur while retrieving weather data.
 */
public class ClientException extends Exception {
    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
