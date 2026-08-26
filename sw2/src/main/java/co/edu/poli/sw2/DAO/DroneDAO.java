package co.edu.poli.sw2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.servicios.ConexionBD;
import co.edu.poli.sw2.servicios.DroneCreator;

/**
 * DAO unico para Drone (clase abstracta). No existe columna "tipo" en la
 * tabla drone: el tipo se determina segun en cual tabla hija aparece el
 * idDrone (droneAgricultura o droneVigilancia).
 *
 * La construccion de las instancias concretas (Agricultura / Vigilancia)
 * se delega siempre a un {@link DroneCreator} (patrón Factory Method):
 * el DAO obtiene el Creator adecuado con DroneCreator.paraTipo(tipo) y
 * le pide que cree el Drone, sin usar "new Agricultura(...)" ni
 * "new Vigilancia(...)" directamente.
 *
 * La conexion se obtiene siempre a traves de ConexionBD.getInstancia()
 * (patrón Singleton): nunca se instancia ConexionBD directamente.
 *
 * Esquema real (segun BD del usuario):
 *   drone(idDrone PK, serial, modelo, fabricante, peso)
 *   droneAgricultura(capacidadTanque, idDrone FK -> drone)
 *   droneVigilancia(capacidadTermica, idDrone FK -> drone)
 */
public class DroneDAO implements CRUD<Drone> {

    // =========================
    // CREATE
    // =========================

    @Override
    public String create(Drone drone) throws Exception {

        String sqlDrone = "INSERT INTO drone "
                + "(idDrone, serial, modelo, fabricante, peso) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getInstancia().getConexion()) {

            try (PreparedStatement ps = conexion.prepareStatement(sqlDrone)) {

                ps.setString(1, drone.getId());
                ps.setString(2, drone.getSerial());
                ps.setString(3, drone.getModelo());
                ps.setString(4, drone.getFabricante());
                ps.setDouble(5, drone.getPeso());

                ps.executeUpdate();
            }

            insertarEnTablaHija(conexion, drone);

            return "Drone creado correctamente";
        }
    }

    // =========================
    // READ ONE
    // =========================

    @Override
    public <K> Drone readone(K id) throws Exception {

        String idStr = id.toString();

        try (Connection conexion = ConexionBD.getInstancia().getConexion()) {

            Drone drone = buscarComoAgricultura(conexion, idStr);

            if (drone != null) {
                return drone;
            }

            return buscarComoVigilancia(conexion, idStr);
        }
    }

    // =========================
    // READ ALL
    // =========================

    @Override
    public List<Drone> readall() throws Exception {

        List<Drone> drones = new ArrayList<>();

        String sqlAgricultura =
                "SELECT d.idDrone, d.serial, d.modelo, d.fabricante, d.peso, "
                + "a.capacidadTanque "
                + "FROM drone d JOIN droneAgricultura a ON d.idDrone = a.idDrone";

        String sqlVigilancia =
                "SELECT d.idDrone, d.serial, d.modelo, d.fabricante, d.peso, "
                + "v.deteccionTermica "
                + "FROM drone d JOIN droneVigilancia v ON d.idDrone = v.idDrone";

        DroneCreator creatorAgricultura = DroneCreator.paraTipo(DroneCreator.TIPO_AGRICULTURA);
        DroneCreator creatorVigilancia = DroneCreator.paraTipo(DroneCreator.TIPO_VIGILANCIA);

        try (Connection conexion = ConexionBD.getInstancia().getConexion()) {

            try (PreparedStatement ps = conexion.prepareStatement(sqlAgricultura);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    drones.add(creatorAgricultura.crearDrone(
                            rs.getString("idDrone"),
                            rs.getString("serial"),
                            rs.getString("modelo"),
                            rs.getString("fabricante"),
                            rs.getDouble("peso"),
                            String.valueOf(rs.getDouble("capacidadTanque"))
                    ));
                }
            }

            try (PreparedStatement ps = conexion.prepareStatement(sqlVigilancia);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    drones.add(creatorVigilancia.crearDrone(
                            rs.getString("idDrone"),
                            rs.getString("serial"),
                            rs.getString("modelo"),
                            rs.getString("fabricante"),
                            rs.getDouble("peso"),
                            String.valueOf(rs.getBoolean("deteccionTermica"))
                    ));
                }
            }
        }

        return drones;
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public String update(Drone drone) throws Exception {

        String sqlDrone = "UPDATE drone SET "
                + "serial = ?, "
                + "modelo = ?, "
                + "fabricante = ?, "
                + "peso = ? "
                + "WHERE idDrone = ?";

        try (Connection conexion = ConexionBD.getInstancia().getConexion()) {

            int filas;

            try (PreparedStatement ps = conexion.prepareStatement(sqlDrone)) {

                ps.setString(1, drone.getSerial());
                ps.setString(2, drone.getModelo());
                ps.setString(3, drone.getFabricante());
                ps.setDouble(4, drone.getPeso());
                ps.setString(5, drone.getId());

                filas = ps.executeUpdate();
            }

            if (filas > 0) {
                actualizarTablaHija(conexion, drone);
                return "Drone actualizado correctamente";
            }

            return "No se encontró el drone";
        }
    }

    // =========================
    // DELETE
    // =========================

    @Override
    public String delete(Object id) throws Exception {

        String idStr = id.toString();

        try (Connection conexion = ConexionBD.getInstancia().getConexion()) {

            // Se borra primero de las tablas hijas por las FK.
            // No afecta filas si el id no existe en esa tabla.
            eliminarDeTablaHija(conexion, "droneAgricultura", idStr);
            eliminarDeTablaHija(conexion, "droneVigilancia", idStr);

            String sql = "DELETE FROM drone WHERE idDrone = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {

                ps.setString(1, idStr);

                int filas = ps.executeUpdate();

                if (filas > 0) {
                    return "Drone eliminado correctamente";
                }

                return "No se encontró el drone";
            }
        }
    }

    // =========================
    // HELPERS
    // =========================

    private void insertarEnTablaHija(Connection conexion, Drone drone) throws Exception {

        if (drone.getTipo().equals(DroneCreator.TIPO_AGRICULTURA)) {

            Agricultura agricultura = (Agricultura) drone;

            String sql = "INSERT INTO droneAgricultura (idDrone, capacidadTanque) VALUES (?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, agricultura.getId());
                ps.setDouble(2, agricultura.getCapacidadTanque());
                ps.executeUpdate();
            }

        } else if (drone.getTipo().equals(DroneCreator.TIPO_VIGILANCIA)) {

            Vigilancia vigilancia = (Vigilancia) drone;

            String sql = "INSERT INTO droneVigilancia (idDrone, deteccionTermica) VALUES (?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, vigilancia.getId());
                ps.setBoolean(2, vigilancia.isDeteccionTermica());
                ps.executeUpdate();
            }

        } else {
            throw new IllegalArgumentException(
                    "Tipo de drone no soportado: " + drone.getTipo());
        }
    }

    private void actualizarTablaHija(Connection conexion, Drone drone) throws Exception {

        if (drone.getTipo().equals(DroneCreator.TIPO_AGRICULTURA)) {

            Agricultura agricultura = (Agricultura) drone;

            String sql = "UPDATE droneAgricultura SET capacidadTanque = ? WHERE idDrone = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setDouble(1, agricultura.getCapacidadTanque());
                ps.setString(2, agricultura.getId());

                if (ps.executeUpdate() == 0) {
                    // No existia fila hija (por ej. cambio de tipo): se inserta
                    insertarEnTablaHija(conexion, drone);
                }
            }

        } else if (drone.getTipo().equals(DroneCreator.TIPO_VIGILANCIA)) {

            Vigilancia vigilancia = (Vigilancia) drone;

            String sql = "UPDATE droneVigilancia SET deteccionTermica = ? WHERE idDrone = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setBoolean(1, vigilancia.isDeteccionTermica());
                ps.setString(2, vigilancia.getId());

                if (ps.executeUpdate() == 0) {
                    insertarEnTablaHija(conexion, drone);
                }
            }

        } else {
            throw new IllegalArgumentException(
                    "Tipo de drone no soportado: " + drone.getTipo());
        }
    }

    private void eliminarDeTablaHija(Connection conexion, String tabla, String id) throws Exception {

        String sql = "DELETE FROM " + tabla + " WHERE idDrone = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private Drone buscarComoAgricultura(Connection conexion, String id) throws Exception {

        String sql = "SELECT d.idDrone, d.serial, d.modelo, d.fabricante, d.peso, "
                + "a.capacidadTanque "
                + "FROM drone d JOIN droneAgricultura a ON d.idDrone = a.idDrone "
                + "WHERE d.idDrone = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return DroneCreator.paraTipo(DroneCreator.TIPO_AGRICULTURA)
                            .crearDrone(
                                    rs.getString("idDrone"),
                                    rs.getString("serial"),
                                    rs.getString("modelo"),
                                    rs.getString("fabricante"),
                                    rs.getDouble("peso"),
                                    String.valueOf(rs.getDouble("capacidadTanque"))
                            );
                }
            }
        }

        return null;
    }

    private Drone buscarComoVigilancia(Connection conexion, String id) throws Exception {

        String sql = "SELECT d.idDrone, d.serial, d.modelo, d.fabricante, d.peso, "
                + "v.deteccionTermica "
                + "FROM drone d JOIN droneVigilancia v ON d.idDrone = v.idDrone "
                + "WHERE d.idDrone = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return DroneCreator.paraTipo(DroneCreator.TIPO_VIGILANCIA)
                            .crearDrone(
                                    rs.getString("idDrone"),
                                    rs.getString("serial"),
                                    rs.getString("modelo"),
                                    rs.getString("fabricante"),
                                    rs.getDouble("peso"),
                                    String.valueOf(rs.getBoolean("deteccionTermica"))
                            );
                }
            }
        }

        return null;
    }
}