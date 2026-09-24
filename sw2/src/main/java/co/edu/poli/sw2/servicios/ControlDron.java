package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Abstraction (patrón Bridge).
 *
 * Punto de entrada que usa el controller para "activar" un modo de
 * control sobre un {@link Drone} ya existente, sin necesitar saber
 * cómo funciona internamente cada modo: eso queda delegado en el
 * Implementor (interfaz {@link DroneControlImplementor} anidada aquí)
 * que implementan ControlBasico / ControlAutonomo y que se le inyecta
 * por constructor.
 *
 * Esto permite agregar nuevos tipos de control en el futuro (por
 * ejemplo, "control asistido") implementando la interfaz Implementor,
 * sin tocar esta clase ni el modelo Drone.
 */
public class ControlDron {

    /**
     * Implementor (patrón Bridge).
     *
     * Define el contrato que deben cumplir las distintas formas de
     * control de un drone (básico, autónomo, ...). ControlDron se
     * apoya en esta interfaz para delegar el "cómo" se controla el
     * drone, separando la jerarquía de abstracción (tipo de control
     * que ve el usuario) de la jerarquía de implementación (mecanismo
     * real de control).
     */
    public interface DroneControlImplementor {

        /**
         * Ejecuta el mecanismo de control concreto y devuelve una
         * descripción legible de lo que hace ese modo de control.
         */
        String ejecutarControl();
    }

    protected final DroneControlImplementor implementor;

    public ControlDron(DroneControlImplementor implementor) {
        if (implementor == null) {
            throw new IllegalArgumentException("El implementor de control no puede ser nulo");
        }
        this.implementor = implementor;
    }

    /**
     * Activa el control sobre el drone recibido y devuelve el mensaje
     * que se le muestra al usuario (identificación del drone + lo que
     * hace el modo de control seleccionado). No persiste nada en base
     * de datos: es una demostración funcional en memoria.
     */
    public String activar(Drone drone) {

        if (drone == null) {
            throw new IllegalArgumentException("Debe seleccionar un drone para aplicar el control");
        }

        return "Drone \"" + drone.getId() + "\" (" + drone.getTipo() + ", modelo " + drone.getModelo() + ")\n\n"
                + implementor.ejecutarControl();
    }
}