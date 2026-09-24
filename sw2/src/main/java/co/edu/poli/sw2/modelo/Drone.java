package co.edu.poli.sw2.modelo;

/**
 * Clase base abstracta para cualquier drone gestionado por la aplicacion.
 *
 * Concentra los atributos comunes a todo drone (identificador, serial,
 * modelo, fabricante y peso) y define el contrato {@link #getTipo()} que
 * cada subclase concreta ({@link Agricultura}, {@link Vigilancia}) debe
 * implementar. Es el "producto" abstracto del patron Factory Method
 * usado por {@code DroneCreator}: el DAO y el Controller trabajan contra
 * esta clase sin conocer la subclase concreta que se esta creando.
 */
public abstract class Drone {

    /** Identificador unico del drone. */
    protected String id;

    /** Numero de serial asignado por el fabricante. */
    protected String serial;

    /** Modelo comercial del drone. */
    protected String modelo;

    /** Fabricante del drone. */
    protected String fabricante;

    /** Peso del drone, en las unidades usadas por el resto de la app. */
    protected double peso;

    /**
     * Constructor vacio, requerido para frameworks/serializacion y para
     * los DAO que arman el objeto campo a campo.
     */
    public Drone() {
    }

    /**
     * Crea un drone con todos sus atributos comunes ya definidos.
     *
     * @param id          identificador del drone
     * @param serial      serial del drone
     * @param modelo      modelo del drone
     * @param fabricante  fabricante del drone
     * @param peso        peso del drone
     */
    public Drone(String id, String serial, String modelo,
                 String fabricante, double peso) {
        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    /**
     * @return el identificador del drone
     */
    public String getId() {
        return id;
    }

    /**
     * @param id nuevo identificador del drone
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return el serial del drone
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial nuevo serial del drone
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return el modelo del drone
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo nuevo modelo del drone
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return el fabricante del drone
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * @param fabricante nuevo fabricante del drone
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * @return el peso del drone
     */
    public double getPeso() {
        return peso;
    }

    /**
     * @param peso nuevo peso del drone
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Cada subclase concreta indica su tipo ("AGRICULTURA" o "VIGILANCIA").
     * Esto se usa en el DAO/Controller para decidir a qué tabla
     * adicional escribir o leer, sin necesidad de encadenar instanceof.
     */
    public abstract String getTipo();

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", serial=" + serial +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", tipo=" + getTipo() +
                '}';
    }
}