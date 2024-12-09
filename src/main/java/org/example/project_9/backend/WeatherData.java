package org.example.project_9.backend;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    // Nested class for the "weather" array in the JSON
    @Setter
    @Getter
    public static class Weather {
        private String description;
    }

    // Nested class for the "main" object in the JSON
    @Setter
    @Getter
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
    }

    // Nested class for the "wind" object in the JSON
    @Setter
    @Getter
    public static class Wind {
        private double speed;
    }
}