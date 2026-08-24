package co.edu.poli.sw2.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConexionBD {

    // Carga el archivo .env ubicado en la raíz del proyecto (junto al pom.xml).
    // ignoreIfMissing() evita que falle si alguien no tiene el archivo (por
    // ejemplo, si prefiere usar variables de entorno reales del sistema).
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private static final String URL = obtenerVariable("DB_URL");
    private static final String USUARIO = obtenerVariable("DB_USER");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD");

    private static Connection conexion;

    private ConexionBD() {
    }

    /**
     * Busca primero en el .env; si no está ahí, intenta con las variables
     * de entorno reales del sistema operativo (System.getenv). Esto permite
     * que el proyecto funcione tanto con un archivo .env local como en un
     * entorno donde las variables ya estén configuradas directamente.
     */
    private static String obtenerVariable(String nombre) {

        String valor = dotenv.get(nombre);

        if (valor == null || valor.isBlank()) {
            valor = System.getenv(nombre);
        }

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Falta la variable " + nombre
                    + ". Define un archivo .env en la raíz del proyecto "
                    + "(ver .env.example) o configura la variable de entorno."
            );
        }

        return valor;
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