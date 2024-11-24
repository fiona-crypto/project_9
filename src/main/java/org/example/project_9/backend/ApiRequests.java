package org.example.project_9.backend;


import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiRequests {
    private static final String APIKEY = "1266a89cb636c8f1530d0145a302e95e";
    public static WeatherStat getWeatherStats(String city, String metric){
        // metric values:
        // Fahrenheit: imperial
        // Celsius: metric
        // Kelvin: standard
        double longitude = 0, latitude = 0;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest geoRequest = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, APIKEY)))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());
            JsonArray jsonGeoResponse = JsonParser.parseString(geoResponse.body()).getAsJsonArray();
            if (!jsonGeoResponse.isEmpty()){
                JsonObject firstCity = jsonGeoResponse.get(0).getAsJsonObject();
                latitude = firstCity.get("lat").getAsDouble();
                longitude = firstCity.get("lon").getAsDouble();

                HttpRequest weatherRequest = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=%s&appid=%s", latitude, longitude, metric, APIKEY)))
                        .GET()
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
                if(weatherResponse.statusCode() == 200 ){
                    // If request went right status code is 200
                    JsonObject jsonWeatherResponse = JsonParser.parseString(weatherResponse.body()).getAsJsonObject();
                    return toWeatherStat(jsonWeatherResponse);
                }
            }
            return null; //bei FrontEnd Fehlermeldung ausgeben

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e); // auch hier bei Frontend Fehlermeldung ausgeben
        }
    }

    public static WeatherStat toWeatherStat(JsonObject jsonResponse){
        WeatherStat weatherStat = new WeatherStat();
        weatherStat.setLongitude(jsonResponse.getAsJsonObject("coord").get("lon").getAsDouble());
        weatherStat.setLatitute(jsonResponse.getAsJsonObject("coord").get("lat").getAsDouble());
        weatherStat.setWeather(new WeatherInfo(
                jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString(),
                jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString()
        ));
        weatherStat.setTemperature(jsonResponse.getAsJsonObject("main").get("temp").getAsDouble());
        weatherStat.setFeelsLike(jsonResponse.getAsJsonObject("main").get("feels_like").getAsDouble());
        weatherStat.setTemperatureMin(jsonResponse.getAsJsonObject("main").get("temp_min").getAsDouble());
        weatherStat.setTemperatureMax(jsonResponse.getAsJsonObject("main").get("temp_max").getAsDouble());
        weatherStat.setPressure(jsonResponse.getAsJsonObject("main").get("pressure").getAsDouble());
        weatherStat.setHumidity(jsonResponse.getAsJsonObject("main").get("humidity").getAsDouble());
        weatherStat.setWind(new WindInfo(
                jsonResponse.getAsJsonObject("wind").get("speed").getAsDouble(),
                jsonResponse.getAsJsonObject("wind").get("deg").getAsDouble()
        ));
        return weatherStat;
    }

}
