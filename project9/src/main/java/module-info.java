module project.project9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.project9 to javafx.fxml;
    exports project.project9;
}