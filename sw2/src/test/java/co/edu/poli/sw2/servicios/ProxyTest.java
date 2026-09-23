package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.DAO.DroneDAO;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración enfocadas en el patrón Proxy aplicado a la
 * eliminación de drones.
 *
 * <p>IMPORTANTE: al igual que {@code DroneDAOTest}, estas son pruebas
 * de INTEGRACIÓN: la ruta de éxito se conecta a la base de datos real
 * a través de {@link DroneDAO} y {@link ServicioProxy}. Requieren
 * MySQL corriendo y accesible con la configuración de
 * {@code ConexionBD}, y la base de datos "sw2" con sus tablas ya
 * creadas.</p>
 *
 * <p>La contraseña usada en las pruebas ("1234") es la que
 * {@link Proxy} usa por defecto cuando la variable de entorno
 * {@code DRONE_DELETE_PASSWORD} no está configurada.</p>
 */
class ProxyTest {

    private Proxy proxy;
    private DroneDAO droneDAO;

    @BeforeEach
    void setUp() {
        proxy = new Proxy();
        droneDAO = new DroneDAO();
    }

    // =========================
    // CONTRASEÑA INCORRECTA: no debe llegar al servicio real
    // =========================

    @Test
    @DisplayName("eliminar con contraseña incorrecta lanza SecurityException")
    void testEliminarConContrasenaIncorrectaLanzaExcepcion() {

        SecurityException excepcion = assertThrows(
                SecurityException.class,
                () -> proxy.eliminar("cualquier-id", "clave-incorrecta")
        );

        assertTrue(
                excepcion.getMessage().contains("Contraseña incorrecta"),
                "El mensaje de la excepción debe indicar que la contraseña es incorrecta"
        );
    }

    @Test
    @DisplayName("eliminar con contraseña nula lanza SecurityException")
    void testEliminarConContrasenaNulaLanzaExcepcion() {

        assertThrows(
                SecurityException.class,
                () -> proxy.eliminar("cualquier-id", null)
        );
    }

    // =========================
    // CONTRASEÑA CORRECTA: sí debe llegar al servicio real
    // =========================

    @Test
    @DisplayName("eliminar con contraseña correcta elimina el drone a través del servicio real")
    void testEliminarConContrasenaCorrectaEliminaDrone() throws Exception {

        String id = generarId("PRX");
        String serial = generarSerial("SER-PRX");

        Agricultura drone = new Agricultura(
                id, serial, "ProxyTestModelo", "ProxyTestFabricante", 10.0, 20.0
        );

        droneDAO.create(drone);

        assertNotNull(
                droneDAO.readone(id),
                "El drone debe existir en la BD antes de intentar eliminarlo"
        );

        String resultado = proxy.eliminar(id, "1234");

        assertEquals("Drone eliminado correctamente", resultado);

        Drone drone2 = droneDAO.readone(id);
        assertNull(drone2, "El drone ya no debe existir después de eliminarlo");
    }

    // =========================
    // HELPERS
    // =========================

    /** Genera un id corto y unico (<=20 caracteres, limite de idDrone). */
    private String generarId(String prefijo) {
        String sufijo = UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();
        return prefijo + "-" + sufijo;
    }

    /** Genera un serial unico (columna serial VARCHAR(50) UNIQUE). */
    private String generarSerial(String prefijo) {
        return prefijo + "-" + UUID.randomUUID();
    }
}