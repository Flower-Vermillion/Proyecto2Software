module sw2.co.edu.poli.sw2 {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens co.edu.poli.sw2.vista to javafx.fxml;
    opens co.edu.poli.sw2.controller to javafx.fxml;

    exports co.edu.poli.sw2.vista;
    exports co.edu.poli.sw2.controller;
}
