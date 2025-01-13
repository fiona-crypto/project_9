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
     */
    public static WeatherData parseWeatherData(String apiResponse) {
        WeatherData weatherData = new WeatherData();

        try {
            // Parse the JSON response to a JsonObject
            JsonObject json = JsonParser.parseString(apiResponse).getAsJsonObject();

            // Debug: Show the entire JSON response
            Logger.log(Logger.Level.DEBUG, "Parsing JSON: " + json);


            // Extract the city name
            if (json.has("name")) {
                weatherData.setCity(json.get("name").getAsString());
                Logger.log(Logger.Level.INFO, "City: " + weatherData.getCity());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'name' not found in JSON.");
            }

            // Extract main section with temperature and related information
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

            // Extract wind data
            if (json.has("wind")) {
                JsonObject wind = json.getAsJsonObject("wind");

                weatherData.setWindSpeed(wind.get("speed").getAsDouble());
                Logger.log(Logger.Level.INFO, "Wind speed: " + weatherData.getWindSpeed());
            } else {
                Logger.log(Logger.Level.WARN, "Field 'wind' not found in JSON.");
            }

            // Extract weather condition description
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
}
