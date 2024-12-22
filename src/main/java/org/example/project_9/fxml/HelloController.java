package org.example.project_9.fxml;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.project_9.backend.*;

public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button addFavoriteButton;

    @FXML
    private ChoiceBox<String> favoritesChoiceBox;

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

    private FavoritesManager favoritesManager;

    @FXML
    public void initialize() {
        initializeFavorites();
        initializeUnitChoiceBox();
        initializeEventHandlers();
    }

    private void initializeFavorites() {
        favoritesChoiceBox.getItems().setAll(FavoritesManager.getFavoriteCities());
    }

    private void initializeUnitChoiceBox() {
        unitChoiceBox.getSelectionModel().selectFirst(); // Standard auf metrisch
    }

    private void initializeEventHandlers() {
        searchButton.setOnAction(event -> fetchWeatherData());
        addFavoriteButton.setOnAction(event -> handleAddFavoriteCity());
        favoritesChoiceBox.setOnAction(event -> handleSelectFavoriteCity());
    }

    private void handleSelectFavoriteCity() {
        String selectedCity = favoritesChoiceBox.getValue();
        if (selectedCity != null && !selectedCity.isEmpty()) {
            searchField.setText(selectedCity);
            fetchWeatherData();
        }
    }

    private void handleAddFavoriteCity() {
        String city = searchField.getText();
        if (city != null && !city.isEmpty()) {
            boolean wasAdded = FavoritesManager.addFavorite(city);

            if (wasAdded) {
                favoritesChoiceBox.getItems().add(city);
            } else {
                ErrorHandler.showErrorAndLog("City already in favorites.", "City already in favorites.");
            }
        }
    }

    private void fetchWeatherData() {
        Logger.log(Logger.Level.DEBUG, "Search starts.");

        String city = searchField.getText();

        // Erste Überprüfung der Eingabe (ungültige Stadtnamen erkennen)
        if (city == null || city.isEmpty() || !isValidCityName(city)) {
            Platform.runLater(() -> {
                cityLabel.setText("Invalid city name.");
            });
            ErrorHandler.showErrorAndLog("Invalid city name.", "Invalid city name: " + city);
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
                Platform.runLater(() -> {
                    cityLabel.setText("No data found.");
                });
                ErrorHandler.showErrorAndLog("No data from server.", "No response from server.");
                return;
            }

            // API-Antwort überprüfen (z.B. '404 - Stadt nicht gefunden')
            JsonObject jsonResponse = JsonParser.parseString(serverResponse).getAsJsonObject();
            if (jsonResponse.has("cod") && jsonResponse.get("cod").getAsString().equals("404")) {
                Platform.runLater(() -> {
                    cityLabel.setText("City not found.");
                });
                ErrorHandler.showErrorAndLog("City not found.", "City not found: " + city);
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
            Platform.runLater(() -> {
                cityLabel.setText(weatherData.getCity());
                temperatureLabel.setText(String.format("%.2f %s", weatherData.getTemperature(), tempUnit));
                weatherConditionLabel.setText(weatherData.getWeatherCondition());
                feelsLikeLabel.setText(String.format("Feels like: %.2f %s", weatherData.getFeelsLike(), tempUnit));
                maximumLabel.setText(String.format("Max: %.2f %s", weatherData.getTempMax(), tempUnit));
                minimumLabel.setText(String.format("Min: %.2f %s", weatherData.getTempMin(), tempUnit));
                airPressureLabel.setText(String.format("Pressure: %d hPa", weatherData.getPressure()));
                humidityLabel.setText(String.format("Humidity: %d%%", weatherData.getHumidity()));
                windSpeedLabel.setText(String.format("Wind Speed: %.2f %s", weatherData.getWindSpeed(), windUnit));
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                cityLabel.setText("Error: " + e.getMessage());
            });
            ErrorHandler.showErrorAndLog("Error fetching weather data: " + e.getMessage(), "Error fetching weather data: " + e.getMessage());
        }
    }

    private boolean isValidCityName(String cityName) {
        return cityName != null && cityName.matches("[a-zA-ZäöüÄÖÜß\\- ']+") && cityName.length() <= 100;
    }

}