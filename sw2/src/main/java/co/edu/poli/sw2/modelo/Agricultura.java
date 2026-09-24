package co.edu.poli.sw2.modelo;

/**
 * Drone concreto especializado en tareas de agricultura.
 *
 * Es uno de los "productos" concretos del patron Factory Method: se
 * construye siempre a traves de {@code DroneCreator.paraTipo(TIPO_AGRICULTURA)}
 * (o de {@code DroneFacade}), nunca instanciando esta clase directamente
 * fuera de esa fabrica. Ademas de los atributos comunes heredados de
 * {@link Drone}, agrega la {@link #capacidadTanque capacidad del tanque}
 * usada para fumigacion/riego.
 */
public class Agricultura extends Drone {

    /** Capacidad del tanque de insumos/agua que carga el drone. */
    private double capacidadTanque;

    /**
     * Constructor vacio, requerido por los DAO que arman el objeto
     * campo a campo a partir del resultado de una consulta.
     */
    public Agricultura() {
        super();
    }

    /**
     * Crea un drone de agricultura con todos sus atributos, incluyendo
     * los comunes heredados de {@link Drone}.
     *
     * @param id               identificador del drone
     * @param serial           serial del drone
     * @param modelo           modelo del drone
     * @param fabricante       fabricante del drone
     * @param peso             peso del drone
     * @param capacidadTanque  capacidad del tanque, propia de Agricultura
     */
    public Agricultura(String id, String serial, String modelo,
                        String fabricante, double peso,
                        double capacidadTanque) {
        super(id, serial, modelo, fabricante, peso);
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * @return la capacidad del tanque del drone
     */
    public double getCapacidadTanque() {
        return capacidadTanque;
    }

    /**
     * @param capacidadTanque nueva capacidad del tanque del drone
     */
    public void setCapacidadTanque(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * @return siempre {@code "AGRICULTURA"}, usado por el DAO/Controller
     *         para identificar el tipo sin recurrir a instanceof
     */
    @Override
    public String getTipo() {
        return "AGRICULTURA";
    }

    @Override
    public String toString() {
        return "Agricultura{" +
                "id='" + id + '\'' +
                ", serial=" + serial +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", capacidadTanque=" + capacidadTanque +
                '}';
    }
}