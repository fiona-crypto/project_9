package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPClient {

    public static String getWeatherData(String city) {
        String serverHost = "localhost";
        int serverPort = 4711;

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(city); // Stadt an den Server senden
            StringBuilder serverResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                serverResponse.append(line);
            }

            return serverResponse.toString();

        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
            return null;
        }
    }
}