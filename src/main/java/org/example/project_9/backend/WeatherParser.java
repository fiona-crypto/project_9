package org.example.project_9.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherParser {

    public static WeatherData parseWeatherData(String apiResponse) {
        WeatherData weatherData = new WeatherData();

        try {
            JsonObject json = JsonParser.parseString(apiResponse).getAsJsonObject();

            // Debug: Zeige die gesamte JSON-Antwort
            System.out.println("Parsing JSON: " + json);

            // City
            if (json.has("name")) {
                weatherData.setCity(json.get("name").getAsString());
                System.out.println("City: " + weatherData.getCity());
            } else {
                System.out.println("Field 'name' not found in JSON.");
            }

            // Main section
            if (json.has("main")) {
                JsonObject main = json.getAsJsonObject("main");

                weatherData.setTemperature(main.get("temp").getAsDouble());
                System.out.println("Temperature: " + weatherData.getTemperature());

                weatherData.setFeelsLike(main.get("feels_like").getAsDouble());
                System.out.println("Feels like: " + weatherData.getFeelsLike());

                weatherData.setTempMin(main.get("temp_min").getAsDouble());
                System.out.println("Temp min: " + weatherData.getTempMin());

                weatherData.setTempMax(main.get("temp_max").getAsDouble());
                System.out.println("Temp max: " + weatherData.getTempMax());

                weatherData.setPressure(main.get("pressure").getAsInt());
                System.out.println("Pressure: " + weatherData.getPressure());

                weatherData.setHumidity(main.get("humidity").getAsInt());
                System.out.println("Humidity: " + weatherData.getHumidity());
            } else {
                System.out.println("Field 'main' not found in JSON.");
            }

            // Wind section
            if (json.has("wind")) {
                JsonObject wind = json.getAsJsonObject("wind");

                weatherData.setWindSpeed(wind.get("speed").getAsDouble());
                System.out.println("Wind speed: " + weatherData.getWindSpeed());
            } else {
                System.out.println("Field 'wind' not found in JSON.");
            }

            // Weather section
            if (json.has("weather")) {
                weatherData.setWeatherCondition(
                        json.getAsJsonArray("weather")
                                .get(0).getAsJsonObject()
                                .get("description").getAsString()
                );
                System.out.println("Weather condition: " + weatherData.getWeatherCondition());
            } else {
                System.out.println("Field 'weather' not found in JSON.");
            }

        } catch (Exception e) {
            System.err.println("Error while parsing data: " + e.getMessage());
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
