package org.example.project_9.backend;

import lombok.Setter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final String LOG_FILE = "application.log";

    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    @Setter
    private static Level minimumLevel = Level.DEBUG;

    public static void log(Level level, String message) {
        // Überprüfen, ob der Level der Nachricht mindestens dem minimumLevel entspricht
        if (level.ordinal() >= minimumLevel.ordinal()) {
            try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {

                // Zeitstempel für die Lognachricht
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                printWriter.printf("[%s] [%s] %s%n", timestamp, level, message);

            } catch (IOException e) {
                System.err.println("Error writing the log file: " + e.getMessage());
            }
        }
    }

}
