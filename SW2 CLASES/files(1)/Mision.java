package co.edu.poli.sw2.modelo;

public class Mision {
    private int id;
    private String nombre;
    private String ubicacion;
    private String fecha;
    private Drone drone;

    // Constructor vacío
    public Mision() {
    }

    // Constructor con todos los atributos
    public Mision(int id, String nombre, String ubicacion, String fecha, Drone drone) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.drone = drone;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
        return "Mision{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", drone=" + (drone != null ? drone.getId() : "null") +
                '}';
    }
}
