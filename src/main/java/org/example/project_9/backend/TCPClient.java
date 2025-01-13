package org.example.project_9.backend;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * A simple TCP client that connects to a server to retrieve weather data.
 * The client sends a request containing the city name and temperature unit,
 * and it expects a JSON response from the server.
 */
public class TCPClient {

    /**
     * Sends a request to the TCP server and retrieves weather data for a given city and unit.
     *
     * @param city  The name of the city for which weather data is requested.
     * @param units The temperature unit ("metric" for Celsius, "imperial" for Fahrenheit).
     * @return The server's response as a JSON string or {@code null} if an error occurs.
     */
    public static String getWeatherData(String city, String units) {
        String serverHost = "localhost";
        int serverPort = 4711;

        Logger.log(Logger.Level.DEBUG, "Preparing to connect to server at " + serverHost + ":" + serverPort);

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Logger.log(Logger.Level.INFO, "Connected to server.");

            // Construct the request string
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            // Construct the request string with the encoded city
            String request = encodedCity + ";" + units;
            Logger.log(Logger.Level.DEBUG, "Sending request to server: " + request);
            // Send the request to the server
            out.println(request);

            // Read the server's response
            StringBuilder serverResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                serverResponse.append(line);
            }

            // Return the server's response as a JSON string
            return serverResponse.toString();

        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error connecting to the server: " + e.getMessage());
            return null;
        }
    }
}