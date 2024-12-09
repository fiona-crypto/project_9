module org.example.project_9 {
    requires javafx.controls;        // For JavaFX controls (like Buttons, TextFields, etc.)
    requires javafx.fxml;           // For JavaFX FXML support
    requires java.net.http;         // For HTTP client functionality
    requires com.google.gson;
    requires lombok;

    opens org.example.project_9.backend to com.google.gson;
    opens org.example.project_9.fxml to javafx.fxml;
    exports org.example.project_9.fxml;
}
