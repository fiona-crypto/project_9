package org.example.project_9.backend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for handling errors by displaying them in the GUI and logging them.
 */
public class ErrorHandler {

    /**
     * Displays an error message to the user through the GUI and logs the error.
     *
     * @param errorMessage The message to display in the alert dialog.
     * @param logMessage   The message to record in the log file.
     */
    public static void showErrorAndLog(String errorMessage, String logMessage) {
        // Display the error in a GUI alert dialog
        showAlert(errorMessage);

        // Log the error message
        Logger.log(Logger.Level.ERROR, logMessage);
    }

    /**
     * Displays an alert dialog with the given error message.
     *
     * @param message The message to display in the alert dialog.
     */
    private static void showAlert(String message) {
        // Create a new Alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
