package co.edu.poli.sw2.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Clase encargada de gestionar la conexión con la base de datos.
 * <p>
 * Implementa el patrón Singleton: existe una única instancia de
 * {@code ConexionBD} durante toda la ejecución del programa, obtenida
 * siempre a través de {@link #getInstancia()}.
 * </p>
 */
public class ConexionBD {

    /**
     * Instancia única de la clase.
     */
    private static ConexionBD instancia;

    /**
     * Conexión activa con la base de datos.
     */
    private Connection conexion;

    // Carga el archivo .env ubicado en la raíz del proyecto (junto al pom.xml).
    // ignoreIfMissing() evita que falle si alguien no tiene el archivo (por
    // ejemplo, si prefiere usar variables de entorno reales del sistema).
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private static final String URL = obtenerVariable("DB_URL");
    private static final String USUARIO = obtenerVariable("DB_USER");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD");

    /**
     * Constructor privado que inicializa la conexión con la base de datos.
     * Al ser privado, nadie fuera de esta clase puede hacer
     * {@code new ConexionBD()}; la única vía de acceso es
     * {@link #getInstancia()}.
     */
    private ConexionBD() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
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

    /**
     * Obtiene la instancia única de la clase.
     * Si la instancia no existe todavía, se crea (inicialización perezosa).
     *
     * @return instancia única de {@code ConexionBD}
     */
    public static synchronized ConexionBD getInstancia() {

        if (instancia == null) {
            instancia = new ConexionBD();
        }

        return instancia;
    }

    /**
     * Retorna la conexión activa con la base de datos.
     * Si la conexión está cerrada o no existe, se abre de nuevo.
     *
     * @return conexión activa de tipo {@link Connection}
     */
    public Connection getConexion() throws SQLException {

        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        }

        return conexion;
    }

    /**
     * Cierra la conexión activa, si existe.
     */
    public void cerrarConexion() {

        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}