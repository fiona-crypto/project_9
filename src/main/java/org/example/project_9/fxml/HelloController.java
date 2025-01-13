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


/**
 * Controller for the JavaFX weather application.
 */
public class HelloController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addFavoriteButton;
    @FXML private ChoiceBox<String> favoritesChoiceBox;
    @FXML private ChoiceBox<String> unitChoiceBox;
    @FXML private Label cityLabel;
    @FXML private Label temperatureLabel;
    @FXML private Label weatherConditionLabel;
    @FXML private Label feelsLikeLabel;
    @FXML private Label maximumLabel;
    @FXML private Label minimumLabel;
    @FXML private Label airPressureLabel;
    @FXML private Label humidityLabel;
    @FXML private Label windSpeedLabel;

    private FavoritesManager favoritesManager;

    /**
     * Initializes the controller. It sets up the favorite cities list,
     * unit selection, and event handlers for buttons and choices.
     */
    @FXML
    public void initialize() {
        initializeFavorites();
        initializeUnitChoiceBox();
        initializeEventHandlers();
    }

    /**
     * Initializes the favorites list in the favorites ChoiceBox.
     */
    private void initializeFavorites() {
        favoritesChoiceBox.getItems().setAll(FavoritesManager.getFavoriteCities());
    }

    /**
     * Initializes the unit choice box to select the first option by default (metric units).
     */
    private void initializeUnitChoiceBox() {
        unitChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets the event handlers for buttons and choice boxes.
     */
    private void initializeEventHandlers() {
        searchButton.setOnAction(event -> fetchWeatherData());
        addFavoriteButton.setOnAction(event -> handleAddFavoriteCity());
        favoritesChoiceBox.setOnAction(event -> handleSelectFavoriteCity());
    }

    /**
     * Handles selecting a favorite city. Updates the search field and fetches data.
     */
    private void handleSelectFavoriteCity() {
        String selectedCity = favoritesChoiceBox.getValue();
        if (selectedCity != null && !selectedCity.isEmpty()) {
            searchField.setText(selectedCity);
            fetchWeatherData();
        }
    }

    /**
     * Handles selecting a favorite city from the list.
     * It updates the search field with the selected city and fetches its weather data.
     */
    private void handleAddFavoriteCity() {
        String city = searchField.getText().trim();  // Trim deletes leading Spaces
        if (city != null && !city.isEmpty()) {
            try {
                // send API-Anfrage
                String serverResponse = TCPClient.getWeatherData(city, "metric");
                JsonObject jsonResponse = JsonParser.parseString(serverResponse).getAsJsonObject();

                // Check if the API returns an error message
                if (jsonResponse.has("cod") && jsonResponse.get("cod").getAsString().equals("404")) {
                    // City not found, show error
                    ErrorHandler.showErrorAndLog("City not found.", "City not found: " + city);
                    return;
                }

                if (!jsonResponse.has("main")) {
                    ErrorHandler.showErrorAndLog("Invalid city data.", "Invalid city data: " + city);
                    return;
                }

                // City is valid, checking if it is already in favorites
                if (FavoritesManager.addFavorite(city)) {
                    favoritesChoiceBox.getItems().add(city);
                    Logger.log(Logger.Level.INFO, "City added to favorites: " + city);
                } else {
                    // City is already in favorites
                    ErrorHandler.showErrorAndLog("City already in favorites.", "City already in favorites: " + city);
                }
            } catch (Exception e) {
                // Error if API request fails
                ErrorHandler.showErrorAndLog("Error verifying city.", "Error verifying city: " + city + ", Message: " + e.getMessage());
            }
        } else {
            // Check if city is invalid
            ErrorHandler.showErrorAndLog("Invalid city name.", "City name is empty or invalid: " + city);
        }
    }


    /**
     * Fetches weather data for the city entered in the search field.
     * This method validates the city input and retrieves data from the server.
     */
    private void fetchWeatherData() {
        Logger.log(Logger.Level.DEBUG, "Search starts.");

        String city = searchField.getText();

        // Validate city input
        if (city == null || city.isEmpty() || !isValidCityName(city)) {
            Platform.runLater(() -> cityLabel.setText("Invalid city name."));
            ErrorHandler.showErrorAndLog("Invalid city name.", "Invalid city name: " + city);
            return;
        }

        // Select the unit from the ChoiceBox (metric or imperial)
        String selectedUnit = unitChoiceBox.getSelectionModel().getSelectedIndex() == 0 ? "metric" : "imperial";

        try {
            // Send the request to fetch weather data from the server
            String serverResponse = TCPClient.getWeatherData(city, selectedUnit);

            Logger.log(Logger.Level.DEBUG, "Server response: " + serverResponse);

            if (serverResponse == null || serverResponse.isEmpty()) {
                Platform.runLater(() -> cityLabel.setText("No data found."));
                ErrorHandler.showErrorAndLog("No data from server.", "No response from server.");
                return;
            }

            // Parse the server's JSON response
            JsonObject jsonResponse = JsonParser.parseString(serverResponse).getAsJsonObject();

            // Set the correct units for temperature and wind speed
            String tempUnit = selectedUnit.equals("metric") ? "°C" : "°F";
            String windUnit = selectedUnit.equals("metric") ? "m/s" : "mph";

            // If city is not found, show an error message
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

            // Parse weather data into a WeatherData object
            WeatherData weatherData = WeatherParser.parseWeatherData(serverResponse);

            // Debugging-Ausgabe
            Logger.log(Logger.Level.DEBUG, "Parsed city: " + weatherData.getCity());
            Logger.log(Logger.Level.DEBUG, "Parsed temperature: " + weatherData.getTemperature());
            Logger.log(Logger.Level.DEBUG, "Parsed weather condition: " + weatherData.getWeatherCondition());


            // Update the GUI with the fetched weather data
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
            Platform.runLater(() -> cityLabel.setText("Error: " + e.getMessage()));
            ErrorHandler.showErrorAndLog("Error fetching weather data: " + e.getMessage(), "Error fetching weather data: " + e.getMessage());
        }
    }

    /**
     * Validates whether the given city name matches the required format.
     *
     * @param cityName The city name to validate.
     * @return True if the city name is valid, false otherwise.
     */
    private boolean isValidCityName(String cityName) {
        return cityName != null && cityName.matches("[a-zA-ZäöüÄÖÜß\\- ']+") && cityName.length() <= 100;
    }

    /**
     * Resets all weather-related labels to default values.
     *
     * @param tempUnit The unit for temperature (e.g., '°C' or '°F').
     * @param windUnit The unit for wind speed (e.g., 'm/s' or 'mph').
     */
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