package co.edu.poli.sw2.modelo;

/**
 * Representa una mision asignada a un {@link Drone}.
 *
 * Guarda los datos basicos de la mision (nombre, ubicacion y fecha) y
 * una referencia al drone que la ejecuta. Instancias de esta clase son
 * el objeto que {@code MisionAdapter} convierte a JSON (patron Adapter)
 * a traves de {@code ServicioJson}.
 */
public class Mision {

    /** Identificador unico de la mision. */
    private int id;

    /** Nombre descriptivo de la mision. */
    private String nombre;

    /** Ubicacion donde se realiza la mision. */
    private String ubicacion;

    /** Fecha en la que se realiza (o realizo) la mision. */
    private String fecha;

    /** Drone asignado a esta mision. */
    private Drone drone;

    /**
     * Constructor vacio, requerido por los DAO/adaptadores que arman el
     * objeto campo a campo.
     */
    public Mision() {
    }

    /**
     * Crea una mision con sus datos basicos (sin drone asignado todavia).
     *
     * @param id         identificador de la mision
     * @param nombre     nombre de la mision
     * @param ubicacion  ubicacion de la mision
     * @param fecha      fecha de la mision
     */
    public Mision(int id, String nombre, String ubicacion, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    /**
     * @return el identificador de la mision
     */
    public int getId() {
        return id;
    }

    /**
     * @param id nuevo identificador de la mision
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return el nombre de la mision
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nuevo nombre de la mision
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return la ubicacion de la mision
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * @param ubicacion nueva ubicacion de la mision
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * @return la fecha de la mision
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha nueva fecha de la mision
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return el drone asignado a la mision
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * @param drone nuevo drone asignado a la mision
     */
    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    @Override
    public String toString() {
        return "Mision{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
