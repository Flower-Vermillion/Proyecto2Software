package co.edu.poli.sw2.modelo;

/**
 * Drone concreto especializado en tareas de vigilancia.
 *
 * Es el otro "producto" concreto del patron Factory Method: se
 * construye siempre a traves de {@code DroneCreator.paraTipo(TIPO_VIGILANCIA)}
 * (o de {@code DroneFacade}), nunca instanciando esta clase directamente
 * fuera de esa fabrica. Ademas de los atributos comunes heredados de
 * {@link Drone}, agrega el indicador de {@link #deteccionTermica deteccion
 * termica}.
 */
public class Vigilancia extends Drone {

    /** Indica si el drone cuenta con camara/sensor de deteccion termica. */
    private boolean deteccionTermica;

    /**
     * Constructor vacio, requerido por los DAO que arman el objeto
     * campo a campo a partir del resultado de una consulta.
     */
    public Vigilancia() {
        super();
    }

    /**
     * Crea un drone de vigilancia con todos sus atributos, incluyendo
     * los comunes heredados de {@link Drone}.
     *
     * @param id                identificador del drone
     * @param serial            serial del drone
     * @param modelo            modelo del drone
     * @param fabricante        fabricante del drone
     * @param peso              peso del drone
     * @param deteccionTermica  indica si el drone tiene deteccion termica,
     *                          propia de Vigilancia
     */
    public Vigilancia(String id, String serial, String modelo,
                       String fabricante, double peso,
                       boolean deteccionTermica) {
        super(id, serial, modelo, fabricante, peso);
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * @return {@code true} si el drone tiene deteccion termica
     */
    public boolean isDeteccionTermica() {
        return deteccionTermica;
    }

    /**
     * @param deteccionTermica nuevo valor de deteccion termica del drone
     */
    public void setDeteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * @return siempre {@code "VIGILANCIA"}, usado por el DAO/Controller
     *         para identificar el tipo sin recurrir a instanceof
     */
    @Override
    public String getTipo() {
        return "VIGILANCIA";
    }

    @Override
    public String toString() {
        return "Vigilancia{" +
                "id='" + id + '\'' +
                ", serial=" + serial +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", deteccionTermica=" + deteccionTermica +
                '}';
    }
}