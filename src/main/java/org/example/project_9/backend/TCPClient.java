package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPClient {

    public static String getWeatherData(String city, String units) {
        String serverHost = "localhost";
        int serverPort = 4711;

        Logger.log(Logger.Level.DEBUG, "Preparing to connect to server at " + serverHost + ":" + serverPort);

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Logger.log(Logger.Level.INFO, "Connected to server.");

            String request = city + ";" + units;
            Logger.log(Logger.Level.DEBUG, "Sending request to server: " + request);
            out.println(request);

            StringBuilder serverResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                serverResponse.append(line);
            }

            return serverResponse.toString();

        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error connecting to the server: " + e.getMessage());
            return null;
        }
    }
}