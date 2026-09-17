package co.edu.poli.sw2.servicios;

/**
 * Component (patrón Decorator).
 *
 * Define el contrato común entre el drone "plano" ({@link DroneWrapper})
 * y cualquier decorador que le agregue características adicionales
 * (por ejemplo, {@link BateriaAdicionalDecorator}), de modo que ambos
 * se puedan tratar de forma uniforme.
 */
public interface DroneComponent {

    /**
     * Descripción del drone, incluyendo cualquier característica
     * adicional que le hayan agregado los decoradores aplicados.
     */
    String getDescripcion();
}