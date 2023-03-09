module com.example.notes {
    requires javafx.controls;
    requires javafx.fxml;


    opens egorov to javafx.fxml;
    exports egorov;
}