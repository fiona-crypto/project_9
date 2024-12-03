package org.example.project_9.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherParser {

    public static WeatherData parseWeatherData(String apiResponse) {
        WeatherData weatherData = new WeatherData();

        try {
            JsonObject json = JsonParser.parseString(apiResponse).getAsJsonObject();

            weatherData.setCity(json.get("name").getAsString());

            JsonObject main = json.getAsJsonObject("main");
            weatherData.setTemperature(main.get("temp").getAsDouble());
            weatherData.setFeelsLike(main.get("feels_like").getAsDouble());
            weatherData.setTempMin(main.get("temp_min").getAsDouble());
            weatherData.setTempMax(main.get("temp_max").getAsDouble());
            weatherData.setPressure(main.get("pressure").getAsInt());
            weatherData.setHumidity(main.get("humidity").getAsInt());

            JsonObject wind = json.getAsJsonObject("wind");
            weatherData.setWindSpeed(wind.get("speed").getAsDouble());

            weatherData.setWeatherCondition(
                    json.getAsJsonArray("weather")
                            .get(0).getAsJsonObject()
                            .get("description").getAsString()
            );

        } catch (Exception e) {
            System.err.println("Error while parsing data: " + e.getMessage());
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
                data.getCity(), data.getTemperature(),data.getWeatherCondition(),
                data.getFeelsLike(), data.getTempMin(), data.getTempMax(),
                data.getPressure(), data.getHumidity(), data.getWindSpeed()
        );
    }
}
