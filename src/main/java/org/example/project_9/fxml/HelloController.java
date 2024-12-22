package org.example.project_9.fxml;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.project_9.backend.*;

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
        favoriteCities = FavoritesManager.loadFavoritesFromLog();
        favoritesChoiceBox.getItems().setAll(favoriteCities);

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
            // Favoriten hinzufügen
            favoriteCities.add(city);
            favoritesChoiceBox.getItems().add(city);  // Favoriten in ChoiceBox anzeigen

            // Speichern in der Logdatei
            FavoritesManager.saveFavorite(city);

            // Log-Ausgabe für das Hinzufügen des Favoriten
            Logger.log(Logger.Level.INFO, "Favorit added: " + city);
        } else {
            Logger.log(Logger.Level.DEBUG, "No city found or already added to favorites.");
        }
    }

    private void fetchWeatherData() {
        Logger.log(Logger.Level.DEBUG, "Search starts.");

        String city = searchField.getText();
        if (city == null || city.isEmpty()) {
            cityLabel.setText("Please type a city.!");
            Logger.log(Logger.Level.ERROR, "No city found.");
            return;
        }

        // Einheit aus der ChoiceBox auswählen
        String selectedUnit = unitChoiceBox.getSelectionModel().getSelectedIndex() == 0 ? "metric" : "imperial";

        try {
            // TCPClient aufrufen, um Daten vom Server abzurufen
            String serverResponse = TCPClient.getWeatherData(city, selectedUnit);

            // Debugging-Ausgabe
            Logger.log(Logger.Level.DEBUG, "Server response: " + serverResponse);

            if (serverResponse == null || serverResponse.isEmpty()) {
                cityLabel.setText("No data found.");
                Logger.log(Logger.Level.ERROR, "No response from server.");
                return;
            }

            // Wetterdaten parsen
            WeatherData weatherData = WeatherParser.parseWeatherData(serverResponse);

            // Debugging-Ausgabe
            Logger.log(Logger.Level.DEBUG, "Parsed city: " + weatherData.getCity());
            Logger.log(Logger.Level.DEBUG, "Parsed temperature: " + weatherData.getTemperature());
            Logger.log(Logger.Level.DEBUG, "Parsed weather condition: " + weatherData.getWeatherCondition());

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
            Logger.log(Logger.Level.ERROR, "Fehler beim Abrufen der Wetterdaten: " + e.getMessage());
        }
    }
}