package co.edu.poli.sw2.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.sw2.modelo.Drone;

public class DroneDAO implements CRUD<Drone> {

    // =========================
    // CREATE
    // =========================

    @Override
    public String create(Drone drone) throws Exception {

        String sql = "INSERT INTO drone "
                   + "(serial, modelo, fabricante, peso) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, drone.getSerial());
            ps.setString(2, drone.getModelo());
            ps.setString(3, drone.getFabricante());
            ps.setInt(4, drone.getPeso());

            ps.executeUpdate();

            return "Drone creado correctamente";
        }
    }

    // =========================
    // READ ONE
    // =========================

    @Override
    public <K> Drone readone(K id) throws Exception {

        String sql = "SELECT idDrone, serial, modelo, fabricante, peso "
                   + "FROM drone WHERE idDrone = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id.toString()));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Drone(
                        rs.getInt("idDrone"),
                        rs.getInt("serial"),
                        rs.getString("modelo"),
                        rs.getString("fabricante"),
                        rs.getInt("peso")
                    );
                }
            }
        }

        return null;
    }

    // =========================
    // READ ALL
    // =========================

    @Override
    public List<Drone> readall() throws Exception {

        List<Drone> drones = new ArrayList<>();

        String sql = "SELECT idDrone, serial, modelo, fabricante, peso "
                   + "FROM drone";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Drone drone = new Drone(
                    rs.getInt("idDrone"),
                    rs.getInt("serial"),
                    rs.getString("modelo"),
                    rs.getString("fabricante"),
                    rs.getInt("peso")
                );

                drones.add(drone);
            }
        }

        return drones;
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public String update(Drone drone) throws Exception {

        String sql = "UPDATE drone SET "
                   + "serial = ?, "
                   + "modelo = ?, "
                   + "fabricante = ?, "
                   + "peso = ? "
                   + "WHERE idDrone = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, drone.getSerial());
            ps.setString(2, drone.getModelo());
            ps.setString(3, drone.getFabricante());
            ps.setInt(4, drone.getPeso());
            ps.setInt(5, drone.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
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

        String sql = "DELETE FROM drone WHERE idDrone = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id.toString()));

            int filas = ps.executeUpdate();

            if (filas > 0) {
                return "Drone eliminado correctamente";
            }

            return "No se encontró el drone";
        }
    }
}