package org.example.project_9.fxml;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.project_9.backend.TCPClient;
import org.example.project_9.backend.WeatherData;
import org.example.project_9.backend.WeatherParser;

public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

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
        // Action für die Such-Schaltfläche definieren
        searchButton.setOnAction(event -> fetchWeatherData());
    }

    private void fetchWeatherData() {
        System.out.println("Suche wurde gestartet."); // Debug-Ausgabe

        String city = searchField.getText();
        if (city == null || city.isEmpty()) {
            cityLabel.setText("Bitte eine Stadt eingeben!");
            return;
        }

        try {
            // TCPClient aufrufen, um Daten vom Server abzurufen
            String serverResponse = TCPClient.getWeatherData(city);

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

            // GUI-Elemente aktualisieren
            cityLabel.setText(weatherData.getCity());
            temperatureLabel.setText(String.format("%.2f °C", weatherData.getTemperature()));
            weatherConditionLabel.setText(weatherData.getWeatherCondition());
            feelsLikeLabel.setText(String.format("Feels like: %.2f °C", weatherData.getFeelsLike()));
            maximumLabel.setText(String.format("Max: %.2f °C", weatherData.getTempMax()));
            minimumLabel.setText(String.format("Min: %.2f °C", weatherData.getTempMin()));
            airPressureLabel.setText(String.format("Pressure: %d hPa", weatherData.getPressure()));
            humidityLabel.setText(String.format("Humidity: %d%%", weatherData.getHumidity()));
            windSpeedLabel.setText(String.format("Wind Speed: %.2f m/s", weatherData.getWindSpeed()));
        } catch (Exception e) {
            cityLabel.setText("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

}