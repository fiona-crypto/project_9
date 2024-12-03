package org.example.project_9.backend;

import lombok.Getter;
import lombok.Setter;

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

}
