package co.edu.poli.sw2.modelo;

/**
 * Representa un sensor "plano" del modelo (id, tipo y fabricante).
 *
 * Es la entidad de datos que {@code SensorWrapper} envuelve para
 * incorporarla al arbol del patron Composite (junto con
 * {@code SensorComposite}), permitiendo tratar un sensor individual y
 * un grupo de sensores de forma uniforme a traves de {@code SensorComponent}.
 */
public class Sensor {

    /** Identificador unico del sensor. */
    private int id;

    /** Tipo de sensor (por ejemplo, termico, GPS, camara, etc.). */
    private String tipo;

    /** Fabricante del sensor. */
    private String fabricante;

    /**
     * Constructor vacio, requerido por los DAO que arman el objeto
     * campo a campo a partir del resultado de una consulta.
     */
    public Sensor() {
    }

    /**
     * Crea un sensor con todos sus datos.
     *
     * @param id          identificador del sensor
     * @param tipo        tipo del sensor
     * @param fabricante  fabricante del sensor
     */
    public Sensor(int id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    /**
     * @return el identificador del sensor
     */
    public int getId() {
        return id;
    }

    /**
     * @param id nuevo identificador del sensor
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return el tipo del sensor
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo nuevo tipo del sensor
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return el fabricante del sensor
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * @param fabricante nuevo fabricante del sensor
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }
}