package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 4711;

        Logger.setMinimumLevel(Logger.Level.DEBUG);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.log(Logger.Level.INFO, "Server started and waiting for connection ...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    Logger.log(Logger.Level.INFO, "Client connected.");

                    String input = in.readLine(); // Eingabe vom Client (Stadt und Einheit)
                    Logger.log(Logger.Level.DEBUG, "Received input from client: " + input);

                    if (input != null && input.contains(";")) {
                        // Splitte Stadt und Einheit
                        String[] parts = input.split(";");
                        String city = parts[0].trim();
                        String units = parts[1].trim();

                        // Rufe Wetterdaten mit Einheit ab
                        String apiResponse = fetchWeatherData(city, units);

                        if (apiResponse != null && !apiResponse.isEmpty() && apiResponse.startsWith("{")) {
                            if (apiResponse.contains("\"cod\":\"404\"")) {
                                Logger.log(Logger.Level.INFO, "City not found in API.");
                                out.println("{\"error\": \"City not found.\"}");  // JSON-Fehlernachricht
                            } else {
                                Logger.log(Logger.Level.DEBUG, "Sending API response to client.");
                                out.println(apiResponse);  // Gib die rohe JSON-Antwort zurück
                            }
                        } else {
                            Logger.log(Logger.Level.ERROR, "Error retrieving data or invalid response.");
                            out.println("{\"error\": \"Error retrieving data or invalid response.\"}");  // JSON-Fehlernachricht
                        }
                    } else {
                        Logger.log(Logger.Level.ERROR, "Invalid request format received.");
                        out.println("{\"error\": \"Invalid request format.\"}"); // Fehler bei ungültiger Anfrage
                    }

                } catch (IOException e) {
                    Logger.log(Logger.Level.ERROR, "Error communicating with the client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Server can't start: " + e.getMessage());
        }
    }

    private static String fetchWeatherData(String city, String units) {
        String apiKey = "2846e091909907b38614eb85000db992";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=" + units;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Logger.log(Logger.Level.DEBUG, "API Response: " + response);

                // Gebe die rohe JSON-Antwort zurück
                return response.toString();  // Rohe JSON-Daten ohne zusätzliche Formatierungen
            }
        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error retrieving data: " + e.getMessage());
            return null;
        }
    }
}