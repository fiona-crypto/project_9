package org.example.project_9.backend;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverHost = "localhost"; // Der Server läuft lokal
        int serverPort = 12345;          // Port des Servers
        String city = "Vienna";          // Stadtname, den der Client an den Server senden soll

        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Nachricht an den Server senden (Stadtname)
            System.out.println("Sende Stadt an den Server...");
            out.println(city);

            // Antwort vom Server lesen (Wetterdaten)
            String serverResponse;
            System.out.println("Warte auf Antwort vom Server...");
            while ((serverResponse = in.readLine()) != null) {
                System.out.println("Antwort vom Server: " + serverResponse);
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Verbinden mit dem Server: " + e.getMessage());
        }
    }
}
