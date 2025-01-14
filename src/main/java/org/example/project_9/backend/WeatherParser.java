package org.example.project_9.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parses weather data from a JSON API response and formats the output.
 */
public class WeatherParser {

    /**
     * Parses the weather data from the given API response.
     * This method extracts various weather attributes from the JSON response,
     * such as city name, temperature, wind speed, and weather conditions.
     *
     * @param apiResponse The API response in JSON format.
     * @return A WeatherData object containing parsed weather information.
     * @throws ParsingException If an error occurs during parsing.
     */
    public static WeatherData parseWeatherData(String apiResponse) throws ParsingException {
        WeatherData weatherData = new WeatherData();

        try {
            // Parse the JSON response to a JsonObject
            JsonObject json = JsonParser.parseString(apiResponse).getAsJsonObject();

            // Debug: Show the entire JSON response
            Logger.log(Logger.Level.INFO, "Parsing JSON: " + json);

            // Extract the city name
            if (json.has("name")) {
                weatherData.setCity(json.get("name").getAsString());
                Logger.log(Logger.Level.INFO, "City: " + weatherData.getCity());
            } else {
                throw new ParsingException("Field 'name' not found in JSON.");
            }

            // Extract main section with temperature and related information
            if (json.has("main")) {
                JsonObject main = json.getAsJsonObject("main");

                weatherData.setTemperature(getJsonDouble(main, "temp", "Temperature"));
                weatherData.setFeelsLike(getJsonDouble(main, "feels_like", "Feels like"));
                weatherData.setTempMin(getJsonDouble(main, "temp_min", "Temp min"));
                weatherData.setTempMax(getJsonDouble(main, "temp_max", "Temp max"));
                weatherData.setPressure(getJsonInt(main, "pressure", "Pressure"));
                weatherData.setHumidity(getJsonInt(main, "humidity", "Humidity"));

                Logger.log(Logger.Level.INFO, "Main section processed.");
            } else {
                throw new ParsingException("Field 'main' not found in JSON.");
            }

            // Extract wind data
            if (json.has("wind")) {
                JsonObject wind = json.getAsJsonObject("wind");
                weatherData.setWindSpeed(getJsonDouble(wind, "speed", "Wind speed"));
            } else {
                throw new ParsingException("Field 'wind' not found in JSON.");
            }

            // Extract weather condition description
            if (json.has("weather")) {
                weatherData.setWeatherCondition(
                        json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString()
                );
                Logger.log(Logger.Level.INFO, "Weather condition: " + weatherData.getWeatherCondition());
            } else {
                throw new ParsingException("Field 'weather' not found in JSON.");
            }

        } catch (IllegalStateException | ClassCastException e) {
            throw new ParsingException("Invalid JSON structure or unexpected value type.", e);
        }
        return weatherData;
    }

    private static double getJsonDouble(JsonObject json, String key, String fieldName) throws ParsingException {
        if (!json.has(key)) {
            throw new ParsingException("Missing field: '" + fieldName + "'");
        }
        try {
            return json.get(key).getAsDouble();
        } catch (NumberFormatException | ClassCastException e) {
            throw new ParsingException("Invalid type for field: '" + fieldName + "'", e);
        }
    }

    private static int getJsonInt(JsonObject json, String key, String fieldName) throws ParsingException {
        if (!json.has(key)) {
            throw new ParsingException("Missing field: '" + fieldName + "'");
        }
        try {
            return json.get(key).getAsInt();
        } catch (NumberFormatException | ClassCastException e) {
            throw new ParsingException("Invalid type for field: '" + fieldName + "'", e);
        }
    }
}