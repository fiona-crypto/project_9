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

    /**
     * Represents the "weather" array in the JSON response.
     * Contains detailed weather descriptions.
     */
    @Setter
    @Getter
    public static class Weather {
        private String description;
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
    }

    /**
     * Represents the "wind" object in the JSON response.
     * Contains wind-related attributes.
     */
    @Setter
    @Getter
    public static class Wind {
        private double speed;
    }
}