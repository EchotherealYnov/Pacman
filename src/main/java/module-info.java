module com.example.projectdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;
    requires jdk.scripting.nashorn;


    opens com.example.projectdesktop to javafx.fxml;
    exports com.example.projectdesktop;
}