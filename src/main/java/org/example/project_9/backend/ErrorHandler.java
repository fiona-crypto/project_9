package org.example.project_9.backend;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorHandler {
    public static void showErrorAndLog(String errorMessage, String logMessage) {
        // Fehler in der GUI anzeigen
        showAlert(errorMessage);

        // Fehler ins Log schreiben
        Logger.log(Logger.Level.ERROR, logMessage);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
