module org.example.project_9 {
    requires javafx.controls;        // For JavaFX controls (like Buttons, TextFields, etc.)
    requires javafx.fxml;           // For JavaFX FXML support
    requires java.net.http;         // For HTTP client functionality
    requires com.google.gson;
    requires lombok;

    // Export the 'fxml' package so that it can be used by other modules
    exports org.example.project_9.fxml;

    // Export the 'backend' package so it can be used by other modules
    exports org.example.project_9.backend;

    // Open the 'backend' package for reflective access (if needed)
    opens org.example.project_9.backend to javafx.fxml; // Only if needed, usually for controllers interacting with backend
}
