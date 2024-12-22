package org.example.project_9.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FavoritesManager {
    private static final String FAVORITES_LOG_FILE = "favorites.log";
    private static List<String> favoriteCities = new ArrayList<>();

    static {
        favoriteCities = loadFavoritesFromLog(); // Initialisiere Favoriten beim Laden der Klasse
    }

    public static List<String> getFavoriteCities() {
        return new ArrayList<>(favoriteCities);
    }

    public static boolean addFavorite(String city) {
        if (city == null || city.isEmpty() || favoriteCities.contains(city)) {
            return false; // Keine Duplikate oder ungültige Einträge hinzufügen
        }
        favoriteCities.add(city);
        saveFavoriteToFile(city);
        return true;
    }

    private static void saveFavoriteToFile(String city) {
        try (FileWriter fileWriter = new FileWriter(FAVORITES_LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(city);
        } catch (IOException e) {
            ErrorHandler.showErrorAndLog("Error saving favorite.", "Error saving favorite.");
        }
    }

    private static List<String> loadFavoritesFromLog() {
        List<String> favorites = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                favorites.add(line);
            }
        } catch (IOException e) {
            ErrorHandler.showErrorAndLog("Error loading favorites.", "Error loading favorites.");
        }
        return favorites;
    }
}