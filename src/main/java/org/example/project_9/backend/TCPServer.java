package org.example.project_9.backend;

import java.io.*;
import java.net.*;

/**
 * A TCP server that listens for client connections, processes requests for weather data,
 * and sends responses. The server communicates using plain text and expects the
 * client to send a city name and temperature unit.
 */
public class TCPServer {

    public static void main(String[] args) {
        int port = 4711;

        // Set the minimum logging level
        Logger.setMinimumLevel(Logger.Level.DEBUG);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.log(Logger.Level.INFO, "Server started and waiting for connections ...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Logger.log(Logger.Level.INFO, "Client connected.");

                // Handle client connection in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Server can't start: " + e.getMessage());
        }
    }

    /**
     * Handles a single client connection.
     *
     * @param clientSocket The socket representing the client connection.
     */
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the client's request
            String input = in.readLine();
            Logger.log(Logger.Level.DEBUG, "Received input from client: " + input);

            // Process valid input
            if (input != null && input.contains(";")) {
                String[] parts = input.split(";");
                String city = parts[0].trim();
                String units = parts[1].trim();

                // Fetch weather data
                String apiResponse = fetchWeatherData(city, units);

                // Send the appropriate response to the client
                if (apiResponse != null && !apiResponse.isEmpty() && apiResponse.startsWith("{")) {
                    if (apiResponse.contains("\"cod\":\"404\"")) {
                        Logger.log(Logger.Level.INFO, "City not found in API.");
                        out.println("{\"error\": \"City not found.\"}");
                    } else {
                        Logger.log(Logger.Level.DEBUG, "Sending API response to client.");
                        out.println(apiResponse);
                    }
                } else {
                    Logger.log(Logger.Level.ERROR, "Error retrieving data or invalid response.");
                    out.println("{\"error\": \"Error retrieving data or invalid response.\"}");
                }
            } else {
                Logger.log(Logger.Level.ERROR, "Invalid request format received.");
                out.println("{\"error\": \"Invalid request format.\"}");
            }

        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error communicating with the client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                Logger.log(Logger.Level.ERROR, "Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * Fetches weather data for a given city and unit.
     *
     * @param city  The name of the city for which the weather data is requested.
     * @param units The unit for temperature (e.g., "metric" for Celsius or "imperial" for Fahrenheit).
     * @return A JSON string containing the weather data or {@code null} if an error occurs.
     */
    private static String fetchWeatherData(String city, String units) {
        String apiKey = "2846e091909907b38614eb85000db992";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=" + units;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Read the API response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Logger.log(Logger.Level.DEBUG, "API Response: " + response);

                // raw JSON response
                return response.toString();
            }
        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error retrieving data: " + e.getMessage());
            return null;
        }
    }
}