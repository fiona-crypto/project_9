package org.example.project_9.backend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherStat {

    private double longitude;
    private double latitute;
    private WeatherInfo weather;
    private double temperature;
    private double feelsLike;
    private double temperatureMin;
    private double temperatureMax;
    private double pressure;
    private double humidity;
    private WindInfo wind;

    @Override
    public String toString() {
        return "WeatherStat{" +
                "\nlongitude=" + longitude +
                "\nlatitute=" + latitute +
                "\nweather=" + weather +
                "\ntemperature=" + temperature +
                "\nfeelsLike=" + feelsLike +
                "\ntemperatureMin=" + temperatureMin +
                "\ntemperatureMax=" + temperatureMax +
                "\npressure=" + pressure +
                "\nhumidity=" + humidity +
                "\nwind=" + wind +
                '}';
    }
}
