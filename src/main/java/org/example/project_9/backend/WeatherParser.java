package org.example.project_9.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherParser {

    public static WeatherData parseWeatherData(String apiResponse) {
        WeatherData weatherData = new WeatherData();

        try {
            JsonObject json = JsonParser.parseString(apiResponse).getAsJsonObject();

            // Debug: Zeige die gesamte JSON-Antwort
            Logger.log(Logger.Level.DEBUG, "Parsing JSON: " + json);

            // City
            if (json.has("name")) {
                weatherData.setCity(json.get("name").getAsString());
                Logger.log(Logger.Level.INFO, "City: " + weatherData.getCity());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'name' not found in JSON.");
            }

            // Main section
            if (json.has("main")) {
                JsonObject main = json.getAsJsonObject("main");

                weatherData.setTemperature(main.get("temp").getAsDouble());
                Logger.log(Logger.Level.INFO, "Temperature: " + weatherData.getTemperature());

                weatherData.setFeelsLike(main.get("feels_like").getAsDouble());
                Logger.log(Logger.Level.INFO, "Feels like: " + weatherData.getFeelsLike());

                weatherData.setTempMin(main.get("temp_min").getAsDouble());
                Logger.log(Logger.Level.INFO, "Temp min: " + weatherData.getTempMin());

                weatherData.setTempMax(main.get("temp_max").getAsDouble());
                Logger.log(Logger.Level.INFO, "Temp max: " + weatherData.getTempMax());

                weatherData.setPressure(main.get("pressure").getAsInt());
                Logger.log(Logger.Level.INFO, "Pressure: " + weatherData.getPressure());

                weatherData.setHumidity(main.get("humidity").getAsInt());
                Logger.log(Logger.Level.INFO, "Humidity: " + weatherData.getHumidity());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'main' not found in JSON.");
            }

            // Wind section
            if (json.has("wind")) {
                JsonObject wind = json.getAsJsonObject("wind");

                weatherData.setWindSpeed(wind.get("speed").getAsDouble());
                Logger.log(Logger.Level.INFO, "Wind speed: " + weatherData.getWindSpeed());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'wind' not found in JSON.");
            }

            // Weather section
            if (json.has("weather")) {
                weatherData.setWeatherCondition(
                        json.getAsJsonArray("weather")
                                .get(0).getAsJsonObject()
                                .get("description").getAsString()
                );
                Logger.log(Logger.Level.INFO, "Weather condition: " + weatherData.getWeatherCondition());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'weather' not found in JSON.");
            }

        } catch (Exception e) {
            Logger.log(Logger.Level.ERROR, "Error while parsing data: " + e.getMessage());
            e.printStackTrace();
        }

        return weatherData;
    }

    public static String formatWeatherData(WeatherData data) {
        return String.format(
                "City: %s%n" +
                        "Temperature: %.2f °C%n" +
                        "Weather Condition: %s%n" +
                        "Feels like: %.2f °C%n" +
                        "Min. Temperature: %.2f °C%n" +
                        "Max. Temperature: %.2f °C%n" +
                        "Air Pressure: %d hPa%n" +
                        "Humidity: %d%%%n" +
                        "Wind Speed: %.2f m/s%n",
                data.getCity() != null ? data.getCity() : "N/A",
                data.getTemperature(),
                data.getWeatherCondition() != null ? data.getWeatherCondition() : "N/A",
                data.getFeelsLike(),
                data.getTempMin(),
                data.getTempMax(),
                data.getPressure(),
                data.getHumidity(),
                data.getWindSpeed()
        );
    }
}
