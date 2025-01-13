package org.example.project_9.backend;

import lombok.Setter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A simple logger utility for logging messages with different levels.
 * Logs are saved to a file and can be filtered based on the set minimum level.
 */
public class Logger {
    private static final String LOG_FILE = "application.log";

    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    @Setter
    private static Level minimumLevel = Level.DEBUG;

    /**
     *
     * @param level   The severity level of the message.
     * @param message The log message.
     */
    public static void log(Level level, String message) {
        // Check if the message's level meets or exceeds the minimum logging level
        if (level.ordinal() >= minimumLevel.ordinal()) {
            try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {

                // Get a timestamp for the log message
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                printWriter.printf("[%s] [%s] %s%n", timestamp, level, message);

            } catch (IOException e) {
                // If writing to the file fails, print the error to the console (stderr)
                System.err.println("Error writing the log file: " + e.getMessage());
            }
        }
    }

}
