package org.example.project_9.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
/*import org.example.project_9.backend.ApiRequests;*/

public class HelloController {
    @FXML
    private TextField searchField;

    @FXML
    private TextField locationLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label weatherConditionLabel;

    @FXML
    private Label feelsLikeLabel;

    @FXML
    private Label maximumLabel;

    @FXML
    private Label minimumLabel;

    @FXML
    private TextField pressureLabel;

    @FXML
    private TextField humidityLabel;

    @FXML
    private TextField windSpeedLabel;

    @FXML
    private TextField windDirectionLabel;

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            System.out.println("Suchfeld ist leer.");
        } else {
            System.out.println("Gesuchte Eingabe: " + query);
            // Hier weitere Logik ergänzen, z. B. Daten filtern oder suchen.
        }
    }


}