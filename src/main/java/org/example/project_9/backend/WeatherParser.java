package org.example.project_9.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherParser {
    public static WeatherData parseWeatherData(String apiResponse) {

        JsonObject jsonObject = new JsonObject();

            // Extrahieren der Wetterdaten
            String cityName = jsonObject.get("name").getAsString();
            double temperature = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            double feelsLike = jsonObject.getAsJsonObject("main").get("feels_like").getAsDouble();
            double tempMin = jsonObject.getAsJsonObject("main").get("temp_min").getAsDouble();
            double tempMax = jsonObject.getAsJsonObject("main").get("temp_max").getAsDouble();
            double humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsDouble();
            double pressure = jsonObject.getAsJsonObject("main").get("pressure").getAsDouble();
            double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
            String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            return new WeatherData(cityName, temperature, feelsLike, tempMin, tempMax, pressure, humidity, windSpeed, weatherCondition);

    }
}

