package org.example.project_9.fxml;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.project_9.backend.TCPClient;
import org.example.project_9.backend.WeatherData;
import org.example.project_9.backend.WeatherParser;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button addFavoriteButton;

    @FXML
    private ChoiceBox<String> favoritesChoiceBox;

    private List<String> favoriteCities = new ArrayList<>();

    @FXML
    private ChoiceBox<String> unitChoiceBox; // Neue ChoiceBox für die Einheitenauswahl

    @FXML
    private Label cityLabel;

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
    private Label airPressureLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windSpeedLabel;

    @FXML
    public void initialize() {
        // Einheitenauswahl vorbereiten

        unitChoiceBox.getSelectionModel().selectFirst(); // Standard auf metrisch

        // Action für die Such-Schaltfläche definieren
        searchButton.setOnAction(event -> fetchWeatherData());

        addFavoriteButton.setOnAction(event -> addFavoriteCity());

        // Aktion für Favoritenauswahl
        favoritesChoiceBox.setOnAction(event -> selectFavoriteCity());
    }

    private void selectFavoriteCity() {
        String selectedCity = favoritesChoiceBox.getValue();
        if (selectedCity != null && !selectedCity.isEmpty()) {
            searchField.setText(selectedCity);
            fetchWeatherData(); // Wetterdaten für die ausgewählte Stadt abrufen
        }
    }

    private void addFavoriteCity() {
        String city = searchField.getText();
        if (city != null && !city.isEmpty() && !favoriteCities.contains(city)) {
            favoriteCities.add(city);
            favoritesChoiceBox.getItems().add(city);
            System.out.println("Favorit hinzugefügt: " + city);
        } else {
            System.out.println("Stadt ist leer oder bereits in den Favoriten.");
        }
    }

    private void fetchWeatherData() {
        System.out.println("Suche wurde gestartet."); // Debug-Ausgabe

        String city = searchField.getText();
        if (city == null || city.isEmpty()) {
            cityLabel.setText("Bitte eine Stadt eingeben!");
            return;
        }

        // Einheit aus der ChoiceBox auswählen
        String selectedUnit = unitChoiceBox.getSelectionModel().getSelectedIndex() == 0 ? "metric" : "imperial";

        try {
            // TCPClient aufrufen, um Daten vom Server abzurufen
            String serverResponse = TCPClient.getWeatherData(city, selectedUnit);

            // Debugging-Ausgabe
            System.out.println("Server response: " + serverResponse);

            if (serverResponse == null || serverResponse.isEmpty()) {
                cityLabel.setText("Keine Daten gefunden.");
                return;
            }

            // Wetterdaten parsen
            WeatherData weatherData = WeatherParser.parseWeatherData(serverResponse);

            // Debugging-Ausgabe
            System.out.println("Parsed city: " + weatherData.getCity());
            System.out.println("Parsed temperature: " + weatherData.getTemperature());
            System.out.println("Parsed weather condition: " + weatherData.getWeatherCondition());

            // Einheitenspezifische Labels
            String tempUnit = selectedUnit.equals("metric") ? "°C" : "°F";
            String windUnit = selectedUnit.equals("metric") ? "m/s" : "mph";

            // GUI-Elemente aktualisieren
            cityLabel.setText(weatherData.getCity());
            temperatureLabel.setText(String.format("%.2f %s", weatherData.getTemperature(), tempUnit));
            weatherConditionLabel.setText(weatherData.getWeatherCondition());
            feelsLikeLabel.setText(String.format("Feels like: %.2f %s", weatherData.getFeelsLike(), tempUnit));
            maximumLabel.setText(String.format("Max: %.2f %s", weatherData.getTempMax(), tempUnit));
            minimumLabel.setText(String.format("Min: %.2f %s", weatherData.getTempMin(), tempUnit));
            airPressureLabel.setText(String.format("Pressure: %d hPa", weatherData.getPressure()));
            humidityLabel.setText(String.format("Humidity: %d%%", weatherData.getHumidity()));
            windSpeedLabel.setText(String.format("Wind Speed: %.2f %s", weatherData.getWindSpeed(), windUnit));
        } catch (Exception e) {
            cityLabel.setText("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}