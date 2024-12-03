package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 4711;
        String city = "Vienna";

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Send city to server...");
            out.println(city);

            String serverResponse;
            System.out.println("Waiting for response from server...");
            while ((serverResponse = in.readLine()) != null) {
                System.out.println("Answer from server: " + serverResponse);
            }

        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }
}
