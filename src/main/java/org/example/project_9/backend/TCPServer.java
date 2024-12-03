package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 4711;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started and waiting auf connection...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connected.");

                    String city = in.readLine();
                    System.out.println("Message from client: " + city);

                    String apiResponse = fetchWeatherData(city);

                    if (apiResponse != null) {
                        WeatherData weatherData = WeatherParser.parseWeatherData(apiResponse);
                        String formattedData = WeatherParser.formatWeatherData(weatherData);
                        out.println(formattedData);
                    } else {
                        out.println("Error retrieving data.");
                    }

                } catch (IOException e) {
                    System.err.println("Error communicating with the client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Server can't start: " + e.getMessage());
        }
    }

    private static String fetchWeatherData(String city) {
        String apiKey = "2846e091909907b38614eb85000db992";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        } catch (IOException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            return null;
        }
    }
}