module sw2.co.edu.poli.sw2 {
    
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires com.google.gson;

    opens co.edu.poli.sw2.vista to javafx.fxml;
    opens co.edu.poli.sw2.controller to javafx.fxml;
    opens co.edu.poli.sw2.modelo to javafx.base, com.google.gson;

    exports co.edu.poli.sw2.vista;
    exports co.edu.poli.sw2.controller;
}