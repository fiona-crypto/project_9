package org.example.project_9.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static final String FAVORITES_LOG_FILE = "favorites.log";  // Neues Logfile für Favoriten

    // Methode zum Speichern eines Favoriten in die Logdatei
    public static void saveFavorite(String city) {
        try (FileWriter fileWriter = new FileWriter(FAVORITES_LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println("Favorit: " + city);

        } catch (IOException e) {
            System.err.println("Error saving favorit: " + e.getMessage());
        }
    }

    // Methode zum Laden der Favoriten aus der Logdatei
    public static List<String> loadFavoritesFromLog() {
        List<String> favoriteCities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Favorit: ")) {
                    favoriteCities.add(line.substring("Favorit: ".length()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading favorites: " + e.getMessage());
        }

        return favoriteCities;
    }
}
