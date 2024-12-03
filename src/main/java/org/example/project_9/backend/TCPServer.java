package org.example.project_9.backend;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server gestartet und wartet auf Verbindungen...");

            while (true) {
                // Warten auf Client-Verbindung
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client verbunden.");

                    // Nachricht vom Client lesen
                    String city = in.readLine();
                    System.out.println("Nachricht vom Client: " + city);

                    // Abrufen von Wetterdaten über die API (z.B. für die angegebene Stadt)
                    String apiResponse = fetchWeatherData(city); // Stadtname vom Client

                    // Wetterdaten parsen und senden
                    if (apiResponse != null) {
                        out.println("API-Daten: " + apiResponse);
                    } else {
                        out.println("Fehler beim Abrufen der Wetterdaten.");
                    }

                } catch (IOException e) {
                    System.err.println("Fehler bei der Kommunikation mit dem Client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Server konnte nicht gestartet werden: " + e.getMessage());
        }
    }

    private static String fetchWeatherData(String city) {
        String apiKey = "2846e091909907b38614eb85000db992"; // Dein API-Schlüssel
        String host = "api.openweathermap.org";
        int port = 443; // HTTPS Port

        try {
            // SSL-Socket für HTTPS-Verbindung erstellen
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // HTTP GET-Anfrage für die OpenWeatherMap-API
            String request = "GET /data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric HTTP/1.1\r\n" +
                    "Host: " + host + "\r\n" +
                    "Connection: close\r\n\r\n";
            out.print(request);
            out.flush();

            // Antwort des Servers einlesen
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            // Gebe die Antwort zurück
            return response.toString();

        } catch (IOException e) {
            System.err.println("Fehler beim Abrufen der Wetterdaten: " + e.getMessage());
            return null;
        }
    }
}
