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
    private static final List<String> favoriteCities;

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
     * @return True if the city was added successfully, false if the city is null, empty, invalid, or already in the list.
     */
    public static boolean addFavorite(String city) {
        if (city == null || city.trim().isEmpty() || !isValidCity(city)) {
            return false; // Invalid input
        }

        String lowerCaseCity = city.toLowerCase();
        if (favoriteCities.stream().map(String::toLowerCase).anyMatch(lowerCaseCity::equals)) {
            return false; // City is already in favorites
        }

        favoriteCities.add(city);
        saveFavoriteToFile(city);
        return true;
    }

    /**
     * Validates if a string is a valid city name.
     *
     * @param city The string to validate.
     * @return True if the string is a valid city name, false otherwise.
     */
    private static boolean isValidCity(String city) {
        // Check if the string contains only letters, spaces, or allowed punctuation (e.g., '-').
        // Exclude strings consisting entirely of digits.
        return city.matches("^[a-zA-ZäöüßÄÖÜ\\s'-]+$");
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