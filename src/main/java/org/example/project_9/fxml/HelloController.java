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
        String city = searchField.getText().trim();  // Trim entfernt führende/folgende Leerzeichen
        if (city != null && !city.isEmpty()) {
            try {
                // API-Anfrage senden
                String serverResponse = TCPClient.getWeatherData(city, "metric");
                JsonObject jsonResponse = JsonParser.parseString(serverResponse).getAsJsonObject();

                // Überprüfen, ob die API eine Fehlermeldung zurückgibt, z. B. 404 - Stadt nicht gefunden
                if (jsonResponse.has("cod") && jsonResponse.get("cod").getAsString().equals("404")) {
                    // Stadt nicht gefunden, Fehler anzeigen
                    ErrorHandler.showErrorAndLog("City not found.", "City not found: " + city);
                    return;
                }

                // Weitere zusätzliche Validierung, die geprüft werden kann, z. B. leere oder ungültige API-Daten
                if (!jsonResponse.has("main")) {
                    ErrorHandler.showErrorAndLog("Invalid city data.", "Invalid city data: " + city);
                    return;
                }

                // Stadt ist gültig, nun sicherstellen, dass sie noch nicht zu den Favoriten hinzugefügt wurde
                if (FavoritesManager.addFavorite(city)) {
                    favoritesChoiceBox.getItems().add(city);
                    Logger.log(Logger.Level.INFO, "City added to favorites: " + city);
                } else {
                    // Stadt schon ein Favorit
                    ErrorHandler.showErrorAndLog("City already in favorites.", "City already in favorites: " + city);
                }
            } catch (Exception e) {
                // Fehlerfall, wenn die API-Anfrage fehlschlägt
                ErrorHandler.showErrorAndLog("Error verifying city.", "Error verifying city: " + city + ", Message: " + e.getMessage());
            }
        } else {
            // Überprüfen, ob die Stadt ungültig (z. B. leer) ist
            ErrorHandler.showErrorAndLog("Invalid city name.", "City name is empty or invalid: " + city);
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

            // Einheitenspezifische Labels
            String tempUnit = selectedUnit.equals("metric") ? "°C" : "°F";
            String windUnit = selectedUnit.equals("metric") ? "m/s" : "mph";

            if (jsonResponse.has("error") && !jsonResponse.get("error").getAsString().isEmpty()){
                Platform.runLater(() -> {
                    cityLabel.setText("City not found");
                    resetLabels(tempUnit, windUnit);
                });
                ErrorHandler.showErrorAndLog("City not found. ", "City not found: " +  city);
                return;
            }

            if (jsonResponse.has("cod") && jsonResponse.get("cod").getAsString().equals("404")) {
                Logger.log(Logger.Level.DEBUG, "City not found.");
                Platform.runLater(() -> {
                    cityLabel.setText("City not found.");
                    resetLabels(tempUnit, windUnit);

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

    private void resetLabels( String tempUnit, String windUnit) {
        temperatureLabel.setText(String.format(" - %s", tempUnit));
        weatherConditionLabel.setText(" - ");
        feelsLikeLabel.setText(String.format(" -  %s", tempUnit));
        maximumLabel.setText(String.format(" -  %s", tempUnit));
        minimumLabel.setText(String.format(" -  %s", tempUnit));
        airPressureLabel.setText(" -  hPa");
        humidityLabel.setText(" - ");
        windSpeedLabel.setText(String.format(" -  %s", windUnit));
    }

}