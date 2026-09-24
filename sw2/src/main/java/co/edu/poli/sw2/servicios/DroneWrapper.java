package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;

/**
 * ConcreteComponent (patrón Decorator).
 *
 * Envuelve un {@link Drone} ya existente (sin modificar el modelo) y
 * ofrece su descripción base. Es el punto de partida sobre el que se
 * pueden ir apilando decoradores como {@link BateriaAdicionalDecorator}.
 */
public class DroneWrapper implements DroneComponent {

    private final Drone drone;

    public DroneWrapper(Drone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("El drone no puede ser nulo");
        }
        this.drone = drone;
    }

    @Override
    public String getDescripcion() {
        return "Drone \"" + drone.getId() + "\" (" + drone.getTipo() + "), modelo "
                + drone.getModelo() + ", fabricante " + drone.getFabricante();
    }
}