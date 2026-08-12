package co.edu.poli.sw2.modelo;

public class Piloto {
    private int id;
    private String nombre;
    private String experiencia;
    private String telefono;
    private Drone drone;

    // Constructor vacío
    public Piloto() {
    }

    // Constructor con todos los atributos
    public Piloto(int id, String nombre, String experiencia, String telefono, Drone drone) {
        this.id = id;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.telefono = telefono;
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

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        return "Piloto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", experiencia='" + experiencia + '\'' +
                ", telefono='" + telefono + '\'' +
                ", drone=" + (drone != null ? drone.getId() : "null") +
                '}';
    }
}
