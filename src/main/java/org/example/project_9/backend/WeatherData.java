package org.example.project_9.backend;

public class WeatherData {
    private String city;
    private double temperature;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private double pressure;
    private double humidity;
    private double windSpeed;
    private String weatherCondition; // Geändert von description auf weatherCondition

    // Konstruktor
    public WeatherData(String city, double temperature, double feelsLike, double tempMin, double tempMax,
                       double pressure, double humidity, double windSpeed, String weatherCondition) {
        this.city = city;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition; // Geändert
    }

    // Getter-Methoden
    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition; // Geändert
    }

    @Override
    public String toString() {
        return "City: " + city + "\n" +
                "Temperature: " + temperature + "°C\n" +
                "Feels Like: " + feelsLike + "°C\n" +
                "Min Temp: " + tempMin + "°C\n" +
                "Max Temp: " + tempMax + "°C\n" +
                "Pressure: " + pressure + " hPa\n" +
                "Humidity: " + humidity + "%\n" +
                "Wind Speed: " + windSpeed + " m/s\n" +
                "Weather Condition: " + weatherCondition; // Geändert
    }
}