module com.example.discordfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens com.example.discordfx to javafx.fxml;
    opens com.example.discordfx.controller to javafx.fxml;
    opens com.example.discordfx.service.dto to javafx.base;
    opens com.example.discordfx.utils.javafx.node to javafx.fxml;

    exports com.example.discordfx;
    opens com.example.discordfx.utils.javafx to javafx.fxml;
}