package co.edu.poli.sw2.servicios;

/**
 * ConcreteImplementor (patrón Bridge).
 *
 * Representa el control autónomo: el drone navega por sí mismo usando
 * rutas programadas y sensores, sin intervención manual continua.
 */
public class ControlAutonomo implements ControlDron.DroneControlImplementor {

    /**
     * Describe el funcionamiento del control autónomo.
     *
     * @return la descripción del modo de control autónomo
     */
    @Override
    public String ejecutarControl() {
        return "Control AUTÓNOMO activado: navegación por GPS con ruta "
                + "preprogramada, evasión automática de obstáculos y "
                + "ejecución de la misión sin intervención manual continua.";
    }
}