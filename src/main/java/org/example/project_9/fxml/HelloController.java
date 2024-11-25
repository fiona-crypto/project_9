package org.example.project_9.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.project_9.backend.ApiRequests;
import org.example.project_9.backend.WeatherStat;

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

    @FXML
    protected void onHelloButtonClick() {

        String city = searchField.getText();

        if (city.isEmpty()) {
            locationLabel.setText("Please enter your city");
            return;
        }

        WeatherStat weatherStat = ApiRequests.getWeatherStats(city,"");

        if (weatherStat != null) {
            locationLabel.setText(city);
            temperatureLabel.setText(String.format("%.2f °C", weatherStat.getTemperature()));
            weatherConditionLabel.setText(weatherStat.getWeather().getMainCondition());
            feelsLikeLabel.setText(String.format("Feels like: %.2f °C", weatherStat.getFeelsLike()));
            pressureLabel.setText(String.format("Pressure: %.0f hPa", weatherStat.getPressure()));
            humidityLabel.setText(String.format("Humidity: %.0f %%", weatherStat.getHumidity()));
            windSpeedLabel.setText(String.format("Wind: %.2f m/s",weatherStat.getWind().getSpeed()));
        } else {
            locationLabel.setText("Please enter your city");
        }
    }
}