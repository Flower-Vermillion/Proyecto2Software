package co.edu.poli.sw2.modelo;

public class Sensor {
    private int id;
    private String tipo;
    private String fabricante;
    private Drone drone;

    // Constructor vacío
    public Sensor() {
    }

    // Constructor con todos los atributos
    public Sensor(int id, String tipo, String fabricante, Drone drone) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
        this.drone = drone;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    // Método toString
    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", drone=" + (drone != null ? drone.getId() : "null") +
                '}';
    }
}
