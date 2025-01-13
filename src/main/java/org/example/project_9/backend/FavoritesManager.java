package org.example.project_9.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages a list of favorite cities, allowing addition and retrieval of favorites.
 * Favorites are persisted in a log file.
 */
public class FavoritesManager {
    private static final String FAVORITES_LOG_FILE = "favorites.log";
    private static List<String> favoriteCities = new ArrayList<>();

    static {
        favoriteCities = loadFavoritesFromLog();
    }

    /**
     * Returns a copy of the list of favorite cities.
     *
     * @return A list of favorite cities.
     */
    public static List<String> getFavoriteCities() {
        return new ArrayList<>(favoriteCities);
    }

    /**
     * Adds a city to the list of favorites.
     *
     * @param city The city to add.
     * @return True if the city was added successfully, false if the city is null, empty, or already in the list.
     */
    public static boolean addFavorite(String city) {
        String lowerCaseCity = city.toLowerCase();
        if (favoriteCities.stream().map(String::toLowerCase).anyMatch(lowerCaseCity::equals)) {
            return false; // Stadt ist bereits in den Favoriten
        }
        favoriteCities.add(city);
        saveFavoriteToFile(city);
        return true;
    }

    /**
     * Saves a city to the log file.
     *
     * @param city The city to save.
     */
    private static void saveFavoriteToFile(String city) {
        try (FileWriter fileWriter = new FileWriter(FAVORITES_LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(city);
        } catch (IOException e) {
            ErrorHandler.showErrorAndLog("Error saving favorite.", "Error saving favorite.");
        }
    }

    /**
     * Loads favorite cities from the log file into memory.
     *
     * @return A list of favorite cities loaded from the log file.
     */
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