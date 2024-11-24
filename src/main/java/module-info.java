module org.example.project_9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.project_9 to javafx.fxml;
    exports org.example.project_9;
}