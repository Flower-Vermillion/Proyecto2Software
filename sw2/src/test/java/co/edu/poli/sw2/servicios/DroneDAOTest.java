package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.DAO.DroneDAO;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPORTANTE: esta es una prueba de INTEGRACIÓN, no una prueba unitaria
 * aislada. Se conecta a la base de datos real (a través de
 * ConexionBD.getConexion()) y los registros que crea quedan
 * guardados en las tablas drone / droneAgricultura / droneVigilancia:
 * no hay @AfterEach ni rollback que los borre.
 *
 * Requisitos para correrla:
 *   - MySQL corriendo y accesible con la configuración de ConexionBD.
 *   - Base de datos "sw2" con las tablas drone, droneAgricultura y
 *     droneVigilancia ya creadas (con la columna deteccionTermica).
 *   - Dependencia de JUnit 5 en el proyecto (org.junit.jupiter:junit-jupiter).
 *
 * Cada corrida genera IDs y seriales únicos (con UUID) para no chocar
 * con las restricciones PRIMARY KEY / UNIQUE si se ejecuta más de una vez.
 */
class DroneDAOTest {

    private DroneDAO droneDAO;

    @BeforeEach
    void setUp() {
        droneDAO = new DroneDAO();
    }

    // =========================
    // CREATE + READ ONE: AGRICULTURA
    // =========================

    @Test
    @DisplayName("Crear un drone de Agricultura lo persiste en drone y droneAgricultura")
    void testCrearYConsultarAgricultura() throws Exception {

        String id = generarId("AGR");
        String serial = generarSerial("SER-AGR");

        Agricultura agricultura = new Agricultura(
                id, serial, "AgroFly X1", "AgroTech", 12.5, 40.0
        );

        String resultado = droneDAO.create(agricultura);
        assertEquals("Drone creado correctamente", resultado);

        Drone recuperado = droneDAO.readone(id);

        assertNotNull(recuperado, "El drone recien creado debe existir en la BD");
        assertInstanceOf(Agricultura.class, recuperado);

        Agricultura agriRecuperado = (Agricultura) recuperado;

        assertEquals(id, agriRecuperado.getId());
        assertEquals(serial, agriRecuperado.getSerial());
        assertEquals("AgroFly X1", agriRecuperado.getModelo());
        assertEquals("AgroTech", agriRecuperado.getFabricante());
        assertEquals(12.5, agriRecuperado.getPeso(), 0.0001);
        assertEquals(40.0, agriRecuperado.getCapacidadTanque(), 0.0001);
        assertEquals("AGRICULTURA", agriRecuperado.getTipo());
    }

    // =========================
    // CREATE + READ ONE: VIGILANCIA
    // =========================

    @Test
    @DisplayName("Crear un drone de Vigilancia lo persiste en drone y droneVigilancia")
    void testCrearYConsultarVigilancia() throws Exception {

        String id = generarId("VIG");
        String serial = generarSerial("SER-VIG");

        Vigilancia vigilancia = new Vigilancia(
                id, serial, "SkyWatch V2", "VigiCorp", 8.3, true
        );

        String resultado = droneDAO.create(vigilancia);
        assertEquals("Drone creado correctamente", resultado);

        Drone recuperado = droneDAO.readone(id);

        assertNotNull(recuperado, "El drone recien creado debe existir en la BD");
        assertInstanceOf(Vigilancia.class, recuperado);

        Vigilancia vigiRecuperado = (Vigilancia) recuperado;

        assertEquals(id, vigiRecuperado.getId());
        assertEquals(serial, vigiRecuperado.getSerial());
        assertEquals("SkyWatch V2", vigiRecuperado.getModelo());
        assertEquals("VigiCorp", vigiRecuperado.getFabricante());
        assertEquals(8.3, vigiRecuperado.getPeso(), 0.0001);
        assertTrue(vigiRecuperado.isDeteccionTermica());
        assertEquals("VIGILANCIA", vigiRecuperado.getTipo());
    }

    // =========================
    // READ ALL
    // =========================

    @Test
    @DisplayName("readall incluye tanto drones de Agricultura como de Vigilancia")
    void testReadAllIncluyeAmbosTipos() throws Exception {

        String idAgricultura = generarId("AGR");
        String idVigilancia = generarId("VIG");

        droneDAO.create(new Agricultura(
                idAgricultura, generarSerial("SER-AGR"),
                "AgroFly X2", "AgroTech", 11.0, 35.0
        ));

        droneDAO.create(new Vigilancia(
                idVigilancia, generarSerial("SER-VIG"),
                "SkyWatch V3", "VigiCorp", 9.1, false
        ));

        List<Drone> todos = droneDAO.readall();

        boolean contieneAgricultura = todos.stream()
                .anyMatch(d -> d instanceof Agricultura
                        && d.getId().equals(idAgricultura));

        boolean contieneVigilancia = todos.stream()
                .anyMatch(d -> d instanceof Vigilancia
                        && d.getId().equals(idVigilancia));

        assertTrue(contieneAgricultura,
                "readall() debe incluir el drone de Agricultura recien creado");
        assertTrue(contieneVigilancia,
                "readall() debe incluir el drone de Vigilancia recien creado");
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    @DisplayName("update modifica en la BD los datos de un drone de Agricultura existente")
    void testUpdateAgricultura() throws Exception {

        String id = generarId("AGR");
        String serial = generarSerial("SER-AGR");

        Agricultura original = new Agricultura(
                id, serial, "AgroFly X3", "AgroTech", 10.0, 30.0
        );

        droneDAO.create(original);

        Agricultura modificado = new Agricultura(
                id, serial, "AgroFly X3 Pro", "AgroTech Ltda", 10.8, 55.5
        );

        String resultado = droneDAO.update(modificado);
        assertEquals("Drone actualizado correctamente", resultado);

        Drone recuperado = droneDAO.readone(id);

        assertNotNull(recuperado);
        assertInstanceOf(Agricultura.class, recuperado);

        Agricultura agriRecuperado = (Agricultura) recuperado;

        assertEquals("AgroFly X3 Pro", agriRecuperado.getModelo());
        assertEquals("AgroTech Ltda", agriRecuperado.getFabricante());
        assertEquals(10.8, agriRecuperado.getPeso(), 0.0001);
        assertEquals(55.5, agriRecuperado.getCapacidadTanque(), 0.0001);
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
