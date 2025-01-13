package org.example.project_9.backend;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents weather data with various attributes such as temperature,
 * wind speed, pressure, and humidity. Provides nested classes for detailed
 * sections of the JSON response.
 */
@Setter
@Getter
public class WeatherData {
    private String city;
    private double temperature;
    private String weatherCondition;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private int pressure;
    private int humidity;
    private double windSpeed;

    // Default constructor
    public WeatherData() {
        this.city = "Unknown City";
        this.temperature = 0.0;
    }

    // Parameterized constructor
    public WeatherData(String city, double temperature, String weatherCondition) {
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
    }

    /**
     * Returns a brief description of the weather data.
     */
    public String getSummary() {
        return String.format("Weather in %s: %.1f°C, %s", city, temperature, weatherCondition);
    }

    /**
     * Represents the "weather" array in the JSON response.
     * Contains detailed weather descriptions.
     */
    @Setter
    @Getter
    public static class Weather {
        private String description;

        public Weather(String description) {
            this.description = description;
        }

        // Overriding toString() to provide a meaningful description
        @Override
        public String toString() {
            return "Weather Description: " + description;
        }
    }

    /**
     * Represents the "main" object in the JSON response.
     * Contains core weather attributes such as temperature, pressure, and humidity.
     */
    @Setter
    @Getter
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        public Main(double temp, int pressure, int humidity) {
            this.temp = temp;
            this.pressure = pressure;
            this.humidity = humidity;
        }

        // Overriding toString() to provide a formatted summary of core weather attributes
        @Override
        public String toString() {
            return String.format("Temp: %.1f°C, Pressure: %dhPa, Humidity: %d%%", temp, pressure, humidity);
        }
    }

    /**
     * Represents the "wind" object in the JSON response.
     * Contains wind-related attributes.
     */
    @Setter
    @Getter
    public static class Wind {
        private double speed;

        public Wind(double speed) {
            this.speed = speed;
        }

        // Overriding toString() to provide wind speed information
        @Override
        public String toString() {
            return "Wind Speed: " + speed + " m/s";
        }
    }

    /**
     * A subclass of WeatherData that includes additional details,
     * demonstrating inheritance and method overriding.
     */
    @Getter
    @Setter
    public static class ExtendedWeatherData extends WeatherData {
        private List<Weather> weatherDetails;

        public ExtendedWeatherData(String city, double temperature, String weatherCondition, List<Weather> weatherDetails) {
            super(city, temperature, weatherCondition);
            this.weatherDetails = weatherDetails;
        }

        @Override
        public String getSummary() {
            StringBuilder summary = new StringBuilder(super.getSummary());
            if (weatherDetails != null && !weatherDetails.isEmpty()) {
                summary.append(", Details: ");
                for (Weather detail : weatherDetails) {
                    summary.append(detail.toString()).append(" ");
                }
            }
            return summary.toString().trim();
        }
    }
}
