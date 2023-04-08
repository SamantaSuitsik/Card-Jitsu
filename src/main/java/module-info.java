module com.example.cardjitsu {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cardjitsu to javafx.fxml;
    exports com.example.cardjitsu;
}