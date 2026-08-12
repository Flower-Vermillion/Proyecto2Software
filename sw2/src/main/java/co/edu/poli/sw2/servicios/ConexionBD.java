package co.edu.poli.sw2.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sw2";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Lony1234";

    private static Connection conexion;

    private ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {

        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
            );
        }

        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
