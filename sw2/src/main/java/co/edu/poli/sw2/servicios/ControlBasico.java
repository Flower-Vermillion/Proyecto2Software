package co.edu.poli.sw2.servicios;

/**
 * ConcreteImplementor (patrón Bridge).
 *
 * Representa el control manual/básico: el piloto maneja el drone
 * directamente, sin rutas ni decisiones automáticas.
 */
public class ControlBasico implements ControlDron.DroneControlImplementor {

    @Override
    public String ejecutarControl() {
        return "Control BÁSICO activado: manejo manual mediante mando/radiocontrol, "
                + "sin rutas automáticas ni asistencia de navegación. "
                + "El piloto es responsable directo de cada maniobra.";
    }
}