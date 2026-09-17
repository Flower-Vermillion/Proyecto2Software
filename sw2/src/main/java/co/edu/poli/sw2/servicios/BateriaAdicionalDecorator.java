package co.edu.poli.sw2.servicios;

/**
 * ConcreteDecorator (patrón Decorator).
 *
 * Envuelve un {@link DroneComponent} (el drone base u otro decorador)
 * y le agrega, sin modificarlo, la descripción de una batería
 * adicional ingresada por el usuario. Es una funcionalidad de
 * demostración en memoria: no se persiste en base de datos.
 */
public class BateriaAdicionalDecorator implements DroneComponent {

    private final DroneComponent componente;
    private final String descripcionBateria;

    public BateriaAdicionalDecorator(DroneComponent componente, String descripcionBateria) {

        if (componente == null) {
            throw new IllegalArgumentException("El componente a decorar no puede ser nulo");
        }
        if (descripcionBateria == null || descripcionBateria.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la descripción de la batería adicional");
        }

        this.componente = componente;
        this.descripcionBateria = descripcionBateria.trim();
    }

    @Override
    public String getDescripcion() {
        return componente.getDescripcion() + "\n+ Batería adicional: " + descripcionBateria;
    }
}