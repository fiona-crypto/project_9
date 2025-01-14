package org.example.project_9.backend;

import java.io.*;
import java.net.Socket;


public class TestClient {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int port = 4711;

        try (Socket socket = new Socket(serverHost, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send city to server
            String city = "Berlin";
            System.out.println("Sending city: " + city);
            out.println(city);

            // Response from server
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server response: " + response);
            }

        } catch (IOException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
        }
    }
}